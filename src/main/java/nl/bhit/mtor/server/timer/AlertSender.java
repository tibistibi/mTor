package nl.bhit.mtor.server.timer;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import nl.bhit.mtor.model.GCMClient;
import nl.bhit.mtor.model.MTorMessage;
import nl.bhit.mtor.model.Project;
import nl.bhit.mtor.model.Status;
import nl.bhit.mtor.model.User;
import nl.bhit.mtor.service.GCMClientManager;
import nl.bhit.mtor.service.MailEngine;
import nl.bhit.mtor.service.MessageManager;
import nl.bhit.mtor.service.ProjectManager;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;

@Component
public class AlertSender {

    @Autowired(
            required = false)
    private ProjectManager projectManager;

    @Autowired
    private MessageManager messageManager;
    @Autowired
    private GCMClientManager gcmClientManager;
    @Autowired
    MailSender mailSender;
    @Autowired
    SimpleMailMessage mailMessage;
    @Autowired
    MailEngine mailEngine;

    private final Sender sender = new Sender("AIzaSyBtsaNuZZ70vvVDFDk7_QpCQ3D-oLN5_oI");

    private static final transient Logger LOG = Logger.getLogger(AlertSender.class);

    public void process() {
        List<Project> projects = projectManager.getWithNonResolvedMessages();

        for (Project project : projects) {
            LOG.trace("working on project: " + project.getId());
            if (project.isMonitoring()) {
                LOG.trace("monitoring is on");
                for (User user : project.getUsers()) {
                    if (!project.hasHeartBeat() && user.getStatusThreshold() != Status.NONE) {
                        sendMailToUser(project, user);
                    }
                    // TODO (tibi) rewrite code to make it more readable
                    /*
                     * if (!project.hasStatus(Status.ERROR)) {
                     * sendMailToUsers(project);
                     * }
                     */
                    Set<MTorMessage> currentMessages = project.getMessages();
                    if (!currentMessages.isEmpty()) {
                        for (MTorMessage message : currentMessages) {
                            if (!message.isAlertSent()) {
                                Status status = message.getStatus();
                                if (status.equals(Status.ERROR) && !message.isResolved()
                                        && user.getStatusThreshold() != Status.NONE) {
                                    sendMessageAlert(project, "An error message has arrived", message.getContent(),
                                            user);
                                    message.setAlertSent(true);
                                    messageManager.save(message);
                                } else if (status.equals(Status.WARN)
                                        && !message.isResolved()
                                        && (user.getStatusThreshold() == Status.INFO || user.getStatusThreshold() == Status.WARN)) {
                                    sendMessageAlert(project, "A warning message has arrived", message.getContent(),
                                            user);
                                    message.setAlertSent(true);
                                    messageManager.save(message);
                                } else if (user.getStatusThreshold() == Status.INFO) {
                                    sendMessageAlert(project, "Info", message.getContent(), user);
                                    message.setAlertSent(true);
                                    messageManager.save(message);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    protected void sendMailToUser(Project project, User user) {
        sendHeartBeatAlert(user.getEmail());
    }

    private void sendHeartBeatAlert(String to) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("sending e-mail to user [" + to + "]...");
        }

        mailMessage.setTo(to + "<" + to + ">");

        mailMessage.setSubject("there is no heartBeat!");
        mailMessage.setText("there is no heartBeat!");
        mailEngine.send(mailMessage);
    }

    private void sendMessageAlert(Project project, String subject, String content, User user) {
        sendGCM(subject, content, user);
        sendMail(subject, content, user);
    }

    private void sendGCM(String subject, String content, User user) {
        LOG.debug("sendGCM: " + subject + " " + content + " " + user);
        // TODO (tibi) get clients by user and not all!
        List<GCMClient> clients = gcmClientManager.getAll();
        for (GCMClient gcmClient : clients) {
            Message.Builder builder = new Message.Builder();
            builder.addData("message", content);
            sendGCMByRegistrationId(gcmClient.getGcmRegistrationId(), builder.build());
        }
    }

    protected void sendGCMByRegistrationId(String registrationId, Message message) {
        Result result = null;
        try {
            LOG.trace("sending to id:" + registrationId);
            result = sender.send(message, registrationId, 5);
        } catch (IOException e) {
            LOG.warn("Could not send GCM (google cloud message): " + e.getMessage());
            LOG.trace("Could not send GCM (google cloud message): " + e.getMessage(), e);
        }
        LOG.trace("Sent message to one device: " + result);
    }

    protected void sendMail(String subject, String content, User user) {
        String to = user.getEmail();
        if (LOG.isDebugEnabled()) {
            LOG.debug("sending e-mail to user [" + to + "]...");
        }

        mailMessage.setTo(to + "<" + to + ">");

        mailMessage.setSubject(subject);
        mailMessage.setText(content);
        mailEngine.send(mailMessage);
    }
}
