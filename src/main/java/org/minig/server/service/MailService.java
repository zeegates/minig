package org.minig.server.service;

import org.minig.security.MailAuthentication;
import org.minig.server.MailFolder;
import org.minig.server.MailMessage;
import org.minig.server.MailMessageAddress;
import org.minig.server.MailMessageList;
import org.minig.server.service.impl.Mime4jAttachmentDataSource;
import org.minig.server.service.impl.helper.MessageMapper;
import org.minig.server.service.impl.helper.mime.Mime4jAttachment;
import org.minig.server.service.impl.helper.mime.Mime4jMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.mail.internet.MimeMessage;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Kamill Sokol
 */
@Service
public class MailService {

    private static final Logger log = LoggerFactory.getLogger(MailService.class);

    @Autowired
    private MailRepository mailRepository;

    @Autowired
    private FolderRepository folderRepository;

    @Autowired
    private MailAuthentication authentication;

    @Autowired
    private AttachmentRepository attachmentRepository;

    // TODO
    @Autowired
    private MessageMapper mapper;

    public MailMessageList findMessagesByFolder(String folder, int page, int pageLength) {
        Page<MimeMessage> mimeMessages = mailRepository.findByFolderOrderByDateDesc(folder, new PageRequest(page, pageLength));
        List<MailMessage> mailMessages = mimeMessages.getContent().stream().map(mapper::convertShort).collect(Collectors.toList());

        MailMessageList mailMessageList = new MailMessageList();
        mailMessageList.setMailList(mailMessages);
        mailMessageList.setPage(mimeMessages.getNumber());
        mailMessageList.setFullLength(mimeMessages.getTotalElements());

        return mailMessageList;
    }

    public MailMessage findMessage(CompositeId id) {
        Assert.notNull(id);

        MailMessage message = mailRepository.read(id);

        if (message == null) {
            throw new NotFoundException();
        }

        return message;
    }

    public Mime4jMessage findById(CompositeId id) {
        Assert.notNull(id);

        Mime4jMessage message = mailRepository.read(id.getFolder(), id.getMessageId());

        if (message == null) {
            throw new NotFoundException();
        } else {
            return message;
        }
    }

    public void deleteMessages(List<CompositeId> messageIdList) {
        Assert.notNull(messageIdList);

        for (CompositeId messageId : messageIdList) {
            deleteMessage(messageId);
        }
    }

    public void deleteMessage(CompositeId messageId) {
        Assert.notNull(messageId);

        MailFolder trashFolder = folderRepository.getTrash();
        MailMessage message = mailRepository.read(messageId);

        if (message != null) {
            if (trashFolder.getId().equals(message.getFolder())) {
                mailRepository.delete(message);
            } else {
                mailRepository.moveMessage(message, trashFolder.getId());
            }
        }
    }

    public void updateMessageFlags(MailMessage source) {
        Assert.notNull(source);
        Assert.notNull(source.getId());

        MailMessage target = mailRepository.read(source);

        if (target == null) {
            throw new NotFoundException();
        }

        if (source.getAnswered() != null) {
            target.setAnswered(source.getAnswered());
        }

        if (source.getRead() != null) {
            target.setRead(source.getRead());
        }

        if (source.getStarred() != null) {
            target.setStarred(source.getStarred());
        }

        if (source.getForwarded() != null) {
            target.setForwarded(source.getForwarded());
        }

        if (source.getMdnSent() != null) {
            target.setMdnSent(source.getMdnSent());
        }

        mailRepository.updateFlags(target);
    }

    public void updateMessagesFlags(MailMessageList source) {
        Assert.notNull(source);
        Assert.notNull(source.getMailList());

        for (MailMessage m : source.getMailList()) {
            try {
                updateMessageFlags(m);
            } catch (Exception e) {
                log.info(e.getMessage());
            }
        }
    }

    public void moveMessageToFolder(CompositeId message, String folder) {
        Assert.notNull(message);
        Assert.hasText(folder);

        mailRepository.moveMessage(message, folder);

        MailMessage mm = mailRepository.readPojo(folder, message.getMessageId());
        mm.setRead(true);
        mailRepository.updateFlags(mm);
    }

    public void moveMessagesToFolder(List<CompositeId> messageIdList, String folder) {
        Assert.notNull(messageIdList);
        Assert.hasText(folder);

        for (CompositeId id : messageIdList) {
            moveMessageToFolder(id, folder);
        }
    }

    public void copyMessagesToFolder(List<CompositeId> messageIdList, String folder) {
        Assert.notNull(messageIdList);
        Assert.hasText(folder);

        for (CompositeId messageId : messageIdList) {
            if (messageId != null && StringUtils.hasText(messageId.getId())) {
                mailRepository.copyMessage(messageId, folder);
            }
        }
    }

    public MailMessage createDraftMessage(MailMessage message) {
        String folderId = folderRepository.getDraft().getId();

        Mime4jMessage mime4jMessage = mapper.toMime4jMessage(message);
        mime4jMessage.setFrom(authentication.getEmailAddress());

        if(StringUtils.hasText(message.getForwardedMessageId())) {
            CompositeId compositeId = mailRepository.findByMessageId(message.getForwardedMessageId());

            if (compositeId != null) {
                List<Mime4jAttachment> attachments = attachmentRepository.read(compositeId);

                for (Mime4jAttachment attachment : attachments) {
                    mime4jMessage.addAttachment(new Mime4jAttachmentDataSource(attachment));
                }
            }
        }

        String saved = mailRepository.save(mime4jMessage, folderId);
        CompositeId compositeId = new CompositeId(folderId, saved);

        MailMessage readPojo = findMessage(compositeId);
        readPojo.setRead(Boolean.TRUE);
        mailRepository.updateFlags(readPojo);

        return readPojo;
    }

    public MailMessage updateDraftMessage(MailMessage message) {
        //TODO maybe saving message and appending attachments from old message is a better approach?
        Mime4jMessage mimeMessage = mailRepository.read(message.getFolder(), message.getMessageId());

        mimeMessage.clearRecipients();
        mimeMessage.clearCc();
        mimeMessage.clearBcc();

        if(message.getTo() != null) {
            for (MailMessageAddress mailMessageAddress : message.getTo()) {
                mimeMessage.addRecipient(mailMessageAddress.getEmail());
            }
        }

        if(message.getCc() != null) {
            for (MailMessageAddress mailMessageAddress : message.getCc()) {
                mimeMessage.addCc(mailMessageAddress.getEmail());
            }
        }

        if(message.getBcc() != null) {
            for (MailMessageAddress mailMessageAddress : message.getBcc()) {
                mimeMessage.addBcc(mailMessageAddress.getEmail());
            }
        }

        mimeMessage.setSubject(message.getSubject());
        mimeMessage.setHtml(message.getHtml());
        mimeMessage.setPlain(message.getPlain());

        mimeMessage.setAskForDispositionNotification(message.getAskForDispositionNotification());
        mimeMessage.setHighPriority(message.getHighPriority());
        mimeMessage.setReceipt(message.getReceipt());
        mimeMessage.setDate(message.getDate());

        //TODO what about other flags?
        String saved = mailRepository.save(mimeMessage, message.getFolder());
        mailRepository.delete(message);
        return mailRepository.readPojo(message.getFolder(), saved);
    }

    public void flagAsAnswered(String messageId) {
        if(!StringUtils.hasText(messageId)) {
            return;
        }

        CompositeId messages = mailRepository.findByMessageId(messageId);
        log.debug("found {} message for messageId {}", messages, messageId);

         mailRepository.setAnsweredFlag(messages, true);
    }

    public void flagAsForwarded(String messageId) {
        if(!StringUtils.hasText(messageId)) {
            return;
        }

        CompositeId compositeId = mailRepository.findByMessageId(messageId);

        if(compositeId == null) {
            log.debug("could not find message for messageId {}", messageId);
            return;
        }

        log.debug("found {} for messageId {}", compositeId.getId(), messageId);

        mailRepository.setForwardedFlag(compositeId, true);
    }
}
