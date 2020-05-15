package com.stas.tourManager.frontend.views;

import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class MyConfirmDialog extends Dialog {
    private Label title = new Label();
    private Label message = new Label();
    private Button cancelButton = new Button();
    private Button confirmButton = new Button();

    public MyConfirmDialog() {
        createHeader();
        createContent();
        createFooter();
    }

    private void createFooter() {
        Button abort = new Button("Abort");
        abort.addClickListener(buttonClickEvent -> close());
        confirmButton = new Button("Confirm");
        confirmButton.addClickListener(buttonClickEvent -> close());
        HorizontalLayout footer = new HorizontalLayout();
        footer.add(abort, confirmButton);
        footer.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        //footer.getStyle().set("background-color", "yellow");
        add(footer);
    }

    private void createContent() {
        VerticalLayout content = new VerticalLayout();
        content.add(message);
        content.setPadding(false);
        //content.getStyle().set("background-color", "red");
        add(content);
    }

    private void createHeader() {
        this.title = new Label();
        Button close = new Button();
        close.setIcon(VaadinIcon.CLOSE.create());
        close.addClickListener(buttonClickEvent -> close());

        HorizontalLayout header = new HorizontalLayout();
        header.add(this.title, close);
        header.setFlexGrow(1, this.title);
        header.setAlignItems(FlexComponent.Alignment.CENTER);
        //header.getStyle().set("background-color", "green");
        add(header);
    }

    public MyConfirmDialog(String title, String content, ComponentEventListener listener) {
        this();
        setTitle(title);
        setQuestion(content);
        addConfirmationListener(listener);
    }

    private void addConfirmationListener(ComponentEventListener listener) {
        confirmButton.addClickListener(listener);
    }

    private void setQuestion(String content) {
        this.message.setText(content);
    }

    private void setTitle(String title) {
        this.title.setText(title);
    }
}
