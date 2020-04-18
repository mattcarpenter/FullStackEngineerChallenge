package net.mattcarpenter.performancereview.service;

import net.mattcarpenter.performancereview.dao.FeedbackRequestTemplateDao;
import net.mattcarpenter.performancereview.dao.FeedbackRequestTemplateFieldDao;
import net.mattcarpenter.performancereview.entity.FeedbackRequestTemplateEntity;
import net.mattcarpenter.performancereview.entity.FeedbackRequestTemplateFieldEntity;
import org.springframework.stereotype.Service;

//@Service
public class SeedService {

    public SeedService(FeedbackRequestTemplateFieldDao feedbackRequestTemplateFieldDao,
                       FeedbackRequestTemplateDao feedbackRequestTemplateDao) {

        feedbackRequestTemplateFieldDao.deleteAll();
        feedbackRequestTemplateDao.deleteAll();

        FeedbackRequestTemplateEntity t1 = new FeedbackRequestTemplateEntity();
        t1.setName("Peer Feedback Template");
        feedbackRequestTemplateDao.save(t1);

        FeedbackRequestTemplateEntity t2 = new FeedbackRequestTemplateEntity();
        t2.setName("Supervisor Feedback Template");
        feedbackRequestTemplateDao.save(t2);

        FeedbackRequestTemplateFieldEntity f1 = new FeedbackRequestTemplateFieldEntity();
        f1.setType("MULTIPLE_CHOICE");
        f1.setChoice1("Strongly agree");
        f1.setChoice2("Strongly disagree");
        f1.setChoiceCount(2);
        f1.setPrompt("Should we fire this employee?");
        f1.setFeedbackRequestTemplate(t1);
        feedbackRequestTemplateFieldDao.save(f1);

        FeedbackRequestTemplateFieldEntity f2 = new FeedbackRequestTemplateFieldEntity();
        f2.setType("TEXT");
        f2.setPrompt("Write something nice.");
        f2.setFeedbackRequestTemplate(t2);
        feedbackRequestTemplateFieldDao.save(f2);

    }
}
