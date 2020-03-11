package app.ui.views;

import app.model.User;
import app.model.UserRole;
import app.service.UserService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.security.access.annotation.Secured;

import java.util.Set;

@Route(value = "admin", layout = MainLayout.class)
@PageTitle("Dance school - Admin")
@Secured("ROLE_ADMIN")
public class Admin extends VerticalLayout implements AfterNavigationObserver {

    private static final long serialVersionUID = 1402318247403655409L;
    private final UserService userService;
    private final Grid<User> grid;
    private Set<UserRole> roles;

    public Admin(UserService userService) {
        this.userService = userService;
        grid = new Grid<>();
        grid.addColumn(User::getId).setHeader("ID")
                .setAutoWidth(true)
                .setSortable(true);
        grid.addColumn(User::getFirstName).setHeader("First Name")
                .setAutoWidth(true)
                .setSortable(true);
        grid.addColumn(User::getLastName).setHeader("Last Name")
                .setAutoWidth(true)
                .setSortable(true);
        grid.addColumn(User::getEmail).setHeader("Email")
                .setAutoWidth(true)
                .setSortable(true);
        grid.addColumn(user -> user.isActive() ? "Yes" : "No").setHeader("Active")
                .setAutoWidth(true)
                .setSortable(true);
        grid.addColumn(User::getRoles).setHeader("Roles")
                .setFlexGrow(10)
                .setSortable(true);
        grid.setItemDetailsRenderer(new ComponentRenderer<>(this::getUserDetails));

        var addUserButton = new Button("Add user", event ->
                new AdminUserEditor(getNewDefaultUser(), roles, userService, this).open());

        add(addUserButton, grid);
    }

    private User getNewDefaultUser() {
        User user = new User();
        user.setRoles(Set.of(UserRole.STUDENT));
        return user;
    }

    private HorizontalLayout getUserDetails(User selectedUser) {
        if (selectedUser.getRoles().contains(UserRole.ADMIN)) {
            return new HorizontalLayout(new Label("Admin profiles cannot be modified!"));
        }
        var deleteButton = new Button("Delete", event -> {
            try {
                userService.deleteUser(selectedUser);
                refreshGridData();
            } catch (RuntimeException e) {
                showNotification(e.getMessage());
            }
        });
        var editButton = new Button("Edit", event ->
                new AdminUserEditor(selectedUser, roles, userService, this).open());
        return new HorizontalLayout(editButton, deleteButton);
    }

    public void refreshGridData() {
        try {
            grid.setItems(userService.getAllUsers());
            roles = userService.getRoles();
        } catch (RuntimeException e) {
            showNotification(e.getMessage());
        }
    }

    @Override
    public void afterNavigation(AfterNavigationEvent event) {
        refreshGridData();
    }

    private void showNotification(String message) {
        var error = new Dialog();
        error.add(new Label(message));
        error.setCloseOnEsc(true);
        error.setCloseOnOutsideClick(true);
        error.open();
    }

}
