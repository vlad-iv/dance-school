package app.ui.views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.RouterLayout;
import org.springframework.security.access.annotation.Secured;

@Secured("ROLE_USER")
public class MainLayout extends VerticalLayout implements RouterLayout {

    private static final long serialVersionUID = -6697848123276949677L;

    public MainLayout() {

        var menuBar = new MenuBar();
        menuBar.addItem("Groups", c -> UI.getCurrent().navigate(GroupList.class));
        menuBar.addItem("Admin", c -> UI.getCurrent().navigate(Admin.class));
        menuBar.addItem("Logout", c -> UI.getCurrent().navigate(LoginView.class));

        add(menuBar);
    }


}
