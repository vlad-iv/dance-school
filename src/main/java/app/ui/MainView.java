package app.ui;

import app.ui.views.GroupList;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import org.springframework.security.access.annotation.Secured;

@Route
@Theme(value = Lumo.class, variant = Lumo.LIGHT)
@PWA(name = "Dance school", shortName = "danceSchool")
@Secured("ROLE_USER")
public class MainView extends VerticalLayout implements AfterNavigationObserver {

    private static final long serialVersionUID = -6410328633056826175L;

    @Override
    public void afterNavigation(AfterNavigationEvent event) {
        UI.getCurrent().navigate(GroupList.class);
    }
}
