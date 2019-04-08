import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class URLPollingJob implements Job {

    private String URL, id;
    static SessionFactory sessionFactoryObj;
    static Session sessionObj;

    private static SessionFactory buildSessionFactory() {
        // Creating Configuration Instance & Passing Hibernate Configuration File
        Configuration configObj = new Configuration();
        configObj.configure("hibernate.cfg.xml");

        // Since Hibernate Version 4.x, ServiceRegistry Is Being Used
        ServiceRegistry serviceRegistryObj = new StandardServiceRegistryBuilder().applySettings(configObj.getProperties()).build();

        // Creating Hibernate SessionFactory Instance
        sessionFactoryObj = configObj.buildSessionFactory(serviceRegistryObj);
        return sessionFactoryObj;
    }

    public void execute(JobExecutionContext context) {
        String text;
        Document doc = null;
        try {
            doc = Jsoup.connect(Main.URL).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Element content = doc.getElementById(Main.id);
        text = content.text();

        sessionObj = buildSessionFactory().openSession();
        sessionObj.beginTransaction();

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();

        POJO pojo = new POJO();
        pojo.setId(dateFormat.format(date));
        pojo.setTag(text);

        sessionObj.save(pojo);

        sessionObj.getTransaction().commit();

        sessionObj.close();
    }
}
