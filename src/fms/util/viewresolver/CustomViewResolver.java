package fms.util.viewresolver;

import org.springframework.core.Ordered;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.AbstractUrlBasedView;
import org.springframework.web.servlet.view.JstlView;
import org.springframework.web.servlet.view.UrlBasedViewResolver;

import java.util.Locale;

public class CustomViewResolver extends UrlBasedViewResolver implements Ordered {
    @Override
    protected View loadView(String viewName, Locale locale) throws Exception {
        AbstractUrlBasedView view = buildView(viewName);
        View viewObj = (View) getApplicationContext().getAutowireCapableBeanFactory().initializeBean(view, viewName);
        if (viewObj instanceof JstlView) {
            JstlView jv = (JstlView) viewObj;
            System.out.println("view Name : "+jv.getBeanName());
            if (jv.getBeanName().indexOf("fms") != -1) {
            	System.out.println("fms != -1 return null");
                return null;
            }
        }
        return viewObj;
    }
}
