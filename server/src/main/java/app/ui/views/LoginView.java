package app.ui.views;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Tag("sa-login-view")
@Route(LoginView.ROUTE)
@PageTitle("Dance school - Login")
public class LoginView extends VerticalLayout {
    public static final String ROUTE = "login";
    private static final long serialVersionUID = -2162437825850193898L;

    private final LoginOverlay login = new LoginOverlay();

    public LoginView() {
        login.setAction("login");
        login.setOpened(true);
        login.setTitle("Dance school");
        login.setDescription("");
        login.setForgotPasswordButtonVisible(false);
        getElement().appendChild(login.getElement());
    }


}
