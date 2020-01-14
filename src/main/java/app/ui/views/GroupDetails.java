package app.ui.views;

import app.model.Group;
import app.model.Schedule;
import app.service.GroupService;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.annotation.Secured;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Route(value = "group-details", layout = MainLayout.class)
@PageTitle("Dance school - Group Details")
@Secured("ROLE_USER")
public final class GroupDetails extends VerticalLayout {

    private static final Logger log = LoggerFactory.getLogger(GroupDetails.class);
    private static final long serialVersionUID = 6522147502315536100L;
    private final GroupService groupService;

    public GroupDetails(GroupService groupService) {
        this.groupService = groupService;
        initializePageData();
    }

    public void initializePageData() {
        try {
            var groupId = (String) ComponentUtil.getData(UI.getCurrent(), "groupId");
            if (groupId == null) {
                throw new RuntimeException("Group ID was not passed between views!");
            }
            var group = groupService.getGroup(Long.valueOf(groupId));
            if (group == null) {
                throw new RuntimeException("Group not found at the server");
            }
            removeAll();
            add(
                    getLabel("Group Details:"),
                    getGroupDetailsForm(group),
                    getSeparator()
//                    getLabel("Schedules:"),
//                    getScheduleGrid(group.getSchedules())
            );
        } catch (Exception e) {
            String err = "Group details cannot be retrieved: ";
            log.error(err, e);
            showNotification(err);
            UI.getCurrent().navigate(GroupList.class);
        }
    }

    private Label getLabel(String text) {
        return new Label(text);
    }

    private HorizontalLayout getSeparator() {
        var separator = new HorizontalLayout();
        separator.setHeight("2em");
        return separator;
    }

    private FormLayout getGroupDetailsForm(Group group) {
        var idField = new TextField();
        var styleField = new TextField();
        var nameField = new TextField();
        var levelField = new TextField();
        var activeField = new Checkbox();
        var startField = new TextField();
        var finishField = new TextField();

        var form = new FormLayout();
        form.addFormItem(idField, "ID");
        form.addFormItem(styleField, "Style");
        form.addFormItem(nameField, "Name");
        form.addFormItem(levelField, "Level");
        form.addFormItem(activeField, "Active");
        form.addFormItem(startField, "Start");
        form.addFormItem(finishField, "Finish");
        form.addFormItem(new Button("Refresh Data", event -> initializePageData()), "");

        var binder = new Binder<Group>();
        binder.forField(idField).bind(u -> String.valueOf(u.getId()), null);
        binder.forField(styleField).bind(Group::getStyle, null);
        binder.forField(nameField).bind(Group::getName, null);
        binder.forField(levelField).bind(Group::getLevel, null);
        binder.forField(activeField).bind(Group::isActive, null);
        binder.forField(startField).bind(u ->
                DateTimeFormatter.ofPattern("YYYY-MM-dd").format(u.getStart()), null);
        binder.forField(finishField).bind(u ->
                DateTimeFormatter.ofPattern("YYYY-MM-dd").format(u.getFinish()), null);
        binder.setReadOnly(true);
        binder.readBean(group);

        return form;
    }

    private Grid<Schedule> getScheduleGrid(List<Schedule> schedules) {
        var grid = new Grid<Schedule>();
        grid.addColumn(s -> s.getDayOfWeek().name()).setHeader("Day").setAutoWidth(true);
        grid.addColumn(Schedule::getStart).setHeader("Start Time").setAutoWidth(true);
        grid.addColumn(Schedule::getFinish).setHeader("Finish Time").setAutoWidth(true);
        grid.setWidthFull();
        grid.setHeightByRows(true);
        grid.setSelectionMode(Grid.SelectionMode.NONE);
        grid.setItems(schedules);
        return grid;
    }

    private void showNotification(String message) {
        var error = new Dialog();
        error.add(new Label(message));
        error.setCloseOnEsc(true);
        error.setCloseOnOutsideClick(true);
        error.open();
    }

}


