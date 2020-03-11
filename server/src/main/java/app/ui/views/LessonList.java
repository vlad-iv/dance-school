package app.ui.views;

import app.model.Lesson;
import app.model.User;
import app.service.LessonService;
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
import java.util.ArrayList;

@Route(value = "lesson-list", layout = MainLayout.class)
@PageTitle("Dance school - Lesson List")
@Secured("ROLE_USER")
public class LessonList extends VerticalLayout implements AfterNavigationObserver {

    private static final long serialVersionUID = 5814895023274210235L;
    private final Grid<Lesson> grid;
    private final LessonService lessonService;


    public LessonList(LessonService lessonService) {
        this.lessonService = lessonService;

        grid = new Grid<>();
        grid.addColumn(o -> DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm").format(o.getDate())).setHeader("Date")
                .setFlexGrow(10)
                .setAutoWidth(true)
                .setSortable(true);
        grid.addColumn(o -> o.getSchedule().toString()).setHeader("Schedule")
                .setAutoWidth(true)
                .setSortable(true);
        grid.addColumn(o -> o.getTeacher().getFirstName() + " " + o.getTeacher().getFirstName()).setHeader("Teacher")
                .setAutoWidth(true)
                .setSortable(true);
        grid.addColumn(Lesson::getStatus).setHeader("Status")
                .setAutoWidth(true)
                .setSortable(true);
//        grid.addColumn(u -> u.getActive() ? "Yes" : "No").setHeader("Active")
//                .setFlexGrow(1)
//                .setSortable(true);

        grid.setWidthFull();
        grid.setHeightByRows(true);

        grid.setSelectionMode(Grid.SelectionMode.NONE);

        grid.setMultiSort(true);
        grid.setItemDetailsRenderer(new ComponentRenderer<>(this::getLessonPreview));
        grid.setSelectionMode(Grid.SelectionMode.NONE);

        add(grid);
    }

    private HorizontalLayout getLessonPreview(Lesson selectedUnit) {
        var detailsButton = new Button("Details", event -> openUnitDetails(selectedUnit.getId()));
        detailsButton.setAutofocus(true);
        detailsButton.setWidth("7em");

        var moduleGrid = new Grid<User>();
        moduleGrid.addColumn(User::getId).setAutoWidth(true);
        moduleGrid.addColumn(User::getFirstName).setFlexGrow(10);
        moduleGrid.addColumn(User::getLastName).setFlexGrow(10);
        moduleGrid.setWidthFull();
        moduleGrid.setHeightByRows(true);
        moduleGrid.setItems(new ArrayList<>(selectedUnit.getStudents()));

        return new HorizontalLayout(detailsButton, moduleGrid);
    }

    private void openUnitDetails(Long lesssonId) {
        ComponentUtil.setData(UI.getCurrent(), "lessonId", lesssonId);
        UI.getCurrent().navigate(LessonDetails.class);
    }

    private void showNotification(String message) {
        var error = new Dialog();
        error.add(new Label(message));
        error.setCloseOnEsc(true);
        error.setCloseOnOutsideClick(true);
        error.open();
    }

    private void refreshGridData() {
        grid.setItems(lessonService.getLessons());
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
