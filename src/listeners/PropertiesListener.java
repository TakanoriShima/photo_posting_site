package listeners;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * Application Lifecycle Listener implementation class PropertiesListener
 *
 */
@WebListener
public class PropertiesListener implements ServletContextListener {

    /**
     * Default constructor.
     */
    public PropertiesListener() {
        // TODO Auto-generated constructor stub
    }

    /**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent arg0) {
        // TODO Auto-generated method stub
    }

    /**
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent arg0) {
        // サーブレットコンテキストを取得
        ServletContext context = arg0.getServletContext();

        // application.propertiesファイルまでのパスの文字列を取得
        String path = context.getRealPath("/META-INF/application.properties");
        try {
            // ファイルパスからInputStream インスタンスを取得
            InputStream is = new FileInputStream(path);
            // プロパティインスタンスを生成
            Properties properties = new Properties();
            // InputStreamインスタンスを使って、プロパティインスタンスにapplication.propertiesファイルの設定値をロード
            properties.load(is);
            // InputStreamを閉じる
            is.close();

            // イテレータを使って、コンテキストに、値をセット
            Iterator<String> pit = properties.stringPropertyNames().iterator();
            while (pit.hasNext()) {
                String pname = pit.next();
                context.setAttribute(pname, properties.getProperty(pname));
            }
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        }
    }

}