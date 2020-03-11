package app.ui.views;

import app.model.Lesson;
import app.model.User;
import app.service.LessonService;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
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

@Route(value = "lesson-details", layout = MainLayout.class)
@PageTitle("Dance school - Lesson Details")
@Secured("ROLE_USER")
public final class LessonDetails extends VerticalLayout {

    private static final Logger log = LoggerFactory.getLogger(LessonDetails.class);
    private static final long serialVersionUID = 6522147502315536100L;
    private final LessonService lessonService;

    public LessonDetails(LessonService lessonService) {
        this.lessonService = lessonService;
        initializePageData();
    }

    public void initializePageData() {
        try {
            var lessonId = (String) ComponentUtil.getData(UI.getCurrent(), "lessonId");
            if (lessonId == null) {
                throw new RuntimeException("Lesson ID was not passed between views!");
            }
            var lesson = lessonService.getLesson(lessonId);
            if (lesson == null) {
                throw new RuntimeException("Lesson not found at the server");
            }
            Long scheduleId = lesson.getSchedule().getId();
            removeAll();
            add(
                    getLabel("Lesson Details:"),
                    getLessonDetailsForm(lesson),
                    getSeparator(),
                    getLabel("Students:"),
                    getStudentGrid(lesson.getStudents()));
        } catch (Exception e) {
            String err = "Lesson details cannot be retrieved: ";
            log.error(err, e);
            showNotification(err);
            UI.getCurrent().navigate(LessonList.class);
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

    private FormLayout getLessonDetailsForm(Lesson lesson) {
        var unitIDField = new TextField();
        var styleField = new TextField();
        var nameField = new TextField();
        var dayField = new TextField();
        var activeField = new TextField();
        var startField = new TextField();
        var finishField = new TextField();

        var form = new FormLayout();
        form.addFormItem(unitIDField, "ID");
        form.addFormItem(styleField, "Style");
        form.addFormItem(nameField, "Name");
        form.addFormItem(dayField, "Day");
        form.addFormItem(activeField, "Active");
        form.addFormItem(startField, "Start");
        form.addFormItem(finishField, "Finish");
        form.addFormItem(new Button("Refresh Data", event -> initializePageData()), "");

        var binder = new Binder<Lesson>();
        binder.forField(unitIDField).bind(u -> String.valueOf(u.getSchedule().getId()), null);
        binder.forField(styleField).bind(u -> u.getSchedule().getGroup().getStyle(), null);
        binder.forField(nameField).bind(u -> u.getSchedule().getGroup().getName(), null);
        binder.forField(dayField).bind(u -> u.getSchedule().getDayOfWeek().name(), null);
        binder.forField(startField).bind(u ->
                DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm").format(u.getSchedule().getGroup().getStart()), null);
        binder.forField(finishField).bind(u ->
                DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm").format(u.getSchedule().getGroup().getFinish()), null);
        binder.setReadOnly(true);
        binder.readBean(lesson);

        return form;
    }

    private Grid<User> getStudentGrid(List<User> students) {
        var grid = new Grid<User>();
        grid.addColumn(User::getRoles).setHeader("Type").setAutoWidth(true);
        grid.addColumn(User::getFirstName).setHeader("First Name").setAutoWidth(true);
        grid.addColumn(User::getLastName).setHeader("Last Name").setAutoWidth(true);
        grid.setWidthFull();
        grid.setHeightByRows(true);
        grid.setSelectionMode(Grid.SelectionMode.NONE);
        grid.setItems(students);
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


