package app.ui.views;

import app.model.Group;
import app.model.Lesson;
import app.service.GroupService;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.UI;
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

import java.time.format.DateTimeFormatter;

@Route(value = "group-list", layout = MainLayout.class)
@PageTitle("Dance school - Group List")
@Secured("ROLE_USER")
public class GroupList extends VerticalLayout implements AfterNavigationObserver {

    private static final long serialVersionUID = 5814895023274210235L;
    private final Grid<Group> grid;
    private final GroupService groupService;


    public GroupList(GroupService groupService) {
        this.groupService = groupService;

        grid = new Grid<>();
        grid.addColumn(Group::getName).setHeader("Name")
                .setAutoWidth(true)
                .setSortable(true);
        grid.addColumn(Group::getStyle).setHeader("Style")
                .setAutoWidth(true)
                .setSortable(true);
        grid.addColumn(Group::getLevel).setHeader("Level")
                .setAutoWidth(true)
                .setSortable(true);
        grid.addColumn(o -> DateTimeFormatter.ofPattern("YYYY-MM-dd").format(o.getStart())).setHeader("Start")
                .setFlexGrow(10)
                .setAutoWidth(true)
                .setSortable(true);
        grid.addColumn(o -> DateTimeFormatter.ofPattern("YYYY-MM-dd").format(o.getFinish())).setHeader("Finish")
                .setFlexGrow(10)
                .setAutoWidth(true)
                .setSortable(true);
//        grid.addColumn(o -> o.getSchedules().toString()).setHeader("Schedule")
//                .setAutoWidth(true)
//                .setSortable(true);
        grid.addColumn(o -> o.getTeacher().getFirstName() + " " + o.getTeacher().getFirstName()).setHeader("Teacher")
                .setAutoWidth(true)
                .setSortable(true);
        grid.addColumn(u -> u.isActive() ? "Yes" : "No").setHeader("Active")
                .setFlexGrow(1)
                .setSortable(true);

        grid.setWidthFull();
        grid.setHeightByRows(true);

        grid.setSelectionMode(Grid.SelectionMode.NONE);

        grid.setMultiSort(true);
        grid.setItemDetailsRenderer(new ComponentRenderer<>(this::getGroupPreview));
        grid.setSelectionMode(Grid.SelectionMode.NONE);

        add(grid);
    }

    private HorizontalLayout getGroupPreview(Group selectedUnit) {
        var detailsButton = new Button("Details", event -> openUnitDetails(selectedUnit.getId()));
        detailsButton.setAutofocus(true);
        detailsButton.setWidth("7em");

        var moduleGrid = new Grid<Lesson>();
        moduleGrid.addColumn(Lesson::getId).setAutoWidth(true);
        moduleGrid.addColumn(Lesson::getDate).setFlexGrow(10);
        moduleGrid.addColumn(Lesson::getStudents).setFlexGrow(10);
        moduleGrid.setWidthFull();
        moduleGrid.setHeightByRows(true);
//        moduleGrid.setItems(new ArrayList<>(selectedUnit.getLessons()));

        return new HorizontalLayout(detailsButton, moduleGrid);
    }

    private void openUnitDetails(Long lesssonId) {
        ComponentUtil.setData(UI.getCurrent(), "groupId", lesssonId);
        UI.getCurrent().navigate(GroupDetails.class);
    }

    private void showNotification(String message) {
        var error = new Dialog();
        error.add(new Label(message));
        error.setCloseOnEsc(true);
        error.setCloseOnOutsideClick(true);
        error.open();
    }

    private void refreshGridData() {
        grid.setItems(groupService.getGroups());
    }

    @Override
    public void afterNavigation(AfterNavigationEvent event) {
        try {
            refreshGridData();
        } catch (RuntimeException e) {
            showNotification("Unit list cannot be retrieved: " + e.getMessage());
        }
    }
}
