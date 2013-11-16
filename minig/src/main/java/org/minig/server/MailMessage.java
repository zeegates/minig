/* ***** BEGIN LICENSE BLOCK *****
 * Version: GPL 2.0
 *
 * The contents of this file are subject to the GNU General Public
 * License Version 2 or later (the "GPL").
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the
 * License.
 *
 * The Initial Developer of the Original Code is
 *   MiniG.org project members
 *
 * ***** END LICENSE BLOCK ***** */

package org.minig.server;

import java.util.Date;
import java.util.List;

import org.minig.server.service.CompositeAttachmentId;
import org.minig.server.service.CompositeId;

public class MailMessage extends CompositeId {

    private String subject;// ok
    private MailMessageBody body = new MailMessageBody(); // ok
    private List<CompositeAttachmentId> attachments; // ok
    private MailMessageAddress sender;// ok
    private List<MailMessageAddress> to;// ok
    private List<MailMessageAddress> cc;// ok
    private List<MailMessageAddress> bcc;// ok
    private List<MailMessageAddress> dispositionNotification;
    private Date date;// ok
    private String mailer;// ok
    private Boolean forwarded; // ok
    private Boolean read;// ok
    private Boolean starred;// ok
    private Boolean answered;// ok
    private Boolean highPriority;// ok
    private Boolean askForDispositionNotification;
    private Boolean receipt;
    private Boolean mdnSent;
    private Boolean deleted;

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public MailMessageBody getBody() {
        return body;
    }

    public void setBody(MailMessageBody body) {
        this.body = body;
    }

    public List<CompositeAttachmentId> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<CompositeAttachmentId> attachments) {
        this.attachments = attachments;
    }

    public MailMessageAddress getSender() {
        return sender;
    }

    public void setSender(MailMessageAddress sender) {
        this.sender = sender;
    }

    public List<MailMessageAddress> getTo() {
        return to;
    }

    public void setTo(List<MailMessageAddress> to) {
        this.to = to;
    }

    public List<MailMessageAddress> getCc() {
        return cc;
    }

    public void setCc(List<MailMessageAddress> cc) {
        this.cc = cc;
    }

    public List<MailMessageAddress> getBcc() {
        return bcc;
    }

    public void setBcc(List<MailMessageAddress> bcc) {
        this.bcc = bcc;
    }

    public List<MailMessageAddress> getDispositionNotification() {
        return dispositionNotification;
    }

    public void setDispositionNotification(List<MailMessageAddress> dispositionNotification) {
        this.dispositionNotification = dispositionNotification;
    }

    public Date getDate() {
        if (date != null) {
            return new Date(date.getTime());
        } else {
            return null;
        }
    }

    public void setDate(Date date) {
        if (date != null) {
            this.date = new Date(date.getTime());
        }
    }

    public String getMailer() {
        return mailer;
    }

    public void setMailer(String mailer) {
        this.mailer = mailer;
    }

    public Boolean getRead() {
        return read;
    }

    public void setRead(Boolean read) {
        this.read = read;
    }

    public Boolean getStarred() {
        return starred;
    }

    public void setStarred(Boolean starred) {
        this.starred = starred;
    }

    public Boolean getAnswered() {
        return answered;
    }

    public void setAnswered(Boolean answered) {
        this.answered = answered;
    }

    public Boolean getHighPriority() {
        return highPriority;
    }

    public void setHighPriority(Boolean highPriority) {
        this.highPriority = highPriority;
    }

    public Boolean getAskForDispositionNotification() {
        return askForDispositionNotification;
    }

    public void setAskForDispositionNotification(Boolean askForDispositionNotification) {
        this.askForDispositionNotification = askForDispositionNotification;
    }

    public Boolean getReceipt() {
        return receipt;
    }

    public void setReceipt(Boolean receipt) {
        this.receipt = receipt;
    }

    public Boolean getForwarded() {
        return forwarded;
    }

    public void setForwarded(Boolean forwarded) {
        this.forwarded = forwarded;
    }

    public Boolean getMdnSent() {
        return mdnSent;
    }

    public void setMdnSent(Boolean mdnSent) {
        this.mdnSent = mdnSent;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

}