package com.stephanie;

import javax.lang.model.element.Element;
import javax.servlet.annotation.WebServlet;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.awt.ActiveEvent;
import java.awt.event.ItemListener;
import java.sql.*;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

// TEST

import java.lang.Object;
import com.vaadin.data.provider.*;

// END TEST

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.data.Result;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.shared.ui.slider.SliderOrientation;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.*;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Grid.Column;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.components.grid.ItemClickListener;

/**
 * Created by SBaille on 05/12/2018 - Sample Assessment
 * 
 * This UI is the application entry point.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is intended to be 
 * overridden to add component to the user interface and initialize non-component functionality.
 */
@Theme("mytheme")
public class MyUI extends UI {
    //Connection to database
    Connection connection = null;

    static int roomCap, totCap = 0, numRoom = 0;
    static boolean roomAlcohol, isAlcoholServed;
    static int totalCapacity=0;
    static String roomsBooked;

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        //final
        final VerticalLayout layout = new VerticalLayout();
        layout.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
        // use if required:  layout.setWidth("75%");

        // "Static" Labels - no updates
        Label logo = new Label("<H1>Marty Party Planners</H1> <p/> <h3>Please enter the details below and click Book</h3> </p><br>", ContentMode.HTML);
        Label studentID = new Label("<h4>B00766809</h4> </p><br>", ContentMode.HTML);

        VerticalLayout vertGrid = new VerticalLayout();
        vertGrid.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);

        //Database connection string
        String connectionString = "jdbc:sqlserver://sampleexamserver1.database.windows.net:1433;" + 
            "database=sampleExam1;" +
            "user=stephanie@sampleexamserver1;" +
            "password=Pa$$w0rd;" +
            "encrypt=true;" + 
            "trustServerCertificate=false;" + 
            "hostNameInCertificate=*.database.windows.net;" + 
            "loginTimeout=30;";

        // ** ** ** 
        try
        {
            //connect with JDBC driver to database
            connection = DriverManager.getConnection(connectionString);

            //add query to db
            ResultSet rs = connection.createStatement().executeQuery("SELECT * FROM roomTable;");

            //START Convert result to a List - refer to the Java class: Room.java
            List<Room> rooms = new ArrayList<Room>();
            while(rs.next())
            {
                //Add a new Room with fields we want to see - might not want all fields available in Db itself
                rooms.add(new Room(rs.getString("name"),
                    rs.getInt("capacity"),
                    rs.getString("feature"),
                    rs.getBoolean("alcohol")));
            }
            //END Convert to List

            // * * * START GRID
            //Create the grid
            Grid<Room> roomGrid = new Grid<>();
            roomGrid.setItems(rooms);
            roomGrid.addColumn(Room::getName).setCaption("Room Name");
            roomGrid.addColumn(Room::getCapacity).setCaption("Capacity");
            roomGrid.addColumn(Room::getFeature).setCaption("Feature");
            roomGrid.addColumn(Room::getAlcohol).setCaption("Alcohol Served?");
            roomGrid.setSizeFull();
            // multiselect mode
            roomGrid.setSelectionMode(SelectionMode.MULTI);
            MultiSelect<Room> selected = roomGrid.asMultiSelect();

            // SelectionListener
            roomGrid.addSelectionListener(event -> {
                totalCapacity = selected.getValue().stream().mapToInt(Room::getCapacity).sum();
                isAlcoholServed = selected.getValue().stream().map(Room::getAlcohol).anyMatch(Predicate.isEqual(true));
                roomsBooked = selected.getValue().stream().map(Room::getName).collect(Collectors.joining(" , ")) ;
                Notification.show("Capacity: " + totalCapacity + "\n" + roomsBooked
                    + " selected" + "\nboolean check: " + isAlcoholServed);
            });
            // * * * END GRID
            

            // * * * START HERE Horizontal layout -  party name/slider/children attending
            HorizontalLayout details = new HorizontalLayout();

            TextField partyName = new TextField();
            partyName.setCaption("Name of Party");

            TextField guest = new TextField();
            //check Placeholder
            guest.setPlaceholder("10");
            guest.setWidth("50%");

            // * * * START SLIDER
            //Add Slider
            Slider numGuestSlider = new Slider("How many people are invited to this party",10,300);
            //numGuestSlider.setCaption("How many people are invited to this party");
            numGuestSlider.setWidth("500px");
            double g = numGuestSlider.getValue();
            numGuestSlider.setValue(10.0);
            numGuestSlider.setMax(300.0);
        
            //Slider value changes
            numGuestSlider.addValueChangeListener(e->{
                double x = numGuestSlider.getValue();
                guest.setValue(""+x);
            });

            guest.addValueChangeListener(e->{
                double x = Double.parseDouble(guest.getValue());
                if (x>numGuestSlider.getMax()){
                    numGuestSlider.setMax(x);
                    numGuestSlider.setWidth(x+"px");
                }
                else if (x<numGuestSlider.getMin()){
                    x = numGuestSlider.getMin();
                }
                numGuestSlider.setValue(x);
            }); //END guest addValue
            // * * * END SLIDER

            //Children attending
            ComboBox<String> children = new ComboBox<>("Children Attending?");
            children.setItems("Yes", "No");
            children.setPlaceholder("No option selected");
            // * * * END HERE Horizontal element name/slider/children

            // * * * START HERE Vertical element book
            Label status = new Label("<strong>Your party is not booked yet.</strong>", ContentMode.HTML); 
            Button book = new Button("Book");
        
            VerticalLayout statusL = new VerticalLayout();
            statusL.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
            statusL.addComponent(status);

            book.addClickListener(e -> {
                if(roomGrid.getSelectedItems().size() == 0) {
                    status.setValue("<strong>Please select at least one room!</strong>");
                    return;
                }
                if (partyName.isEmpty()) {
                    status.setValue("<strong>Please enter party name.</strong>");
                    return;
                }
                if (!children.getSelectedItem().isPresent()) {
                    status.setValue("<strong>Please confirm if children attending your party</strong>");
                    return;
                }
                if (children.getValue() == "Yes" && isAlcoholServed == true) {
                        status.setValue("<strong>You cannot select any rooms serving alcohol if children are attending.</strong>");
                    return;
                }
                if (totalCapacity < numGuestSlider.getValue()) {
                    status.setValue("<strong>You have selected rooms with a max capacity of " + totalCapacity + " which is not enough to hold" + numGuestSlider.getValue().intValue() + ".</strong>");
                    return;
                }
                status.setValue("<h3>Success! The party is booked now</h3>");
            statusL.addComponent(status);
            });
        // * * * END HERE Vertical element book

        //Master Layout
        details.addComponents(partyName, numGuestSlider, children);
        //VerticalLayout booking = new VerticalLayout();
        //booking.addComponents(book, statusL);
        vertGrid.addComponent(roomGrid);
        layout.addComponents(logo, details, book, statusL, vertGrid, studentID);
        
        }
        catch (Exception e)
        {
            layout.addComponent(new Label(e.getMessage()));
        }
        
        setContent(layout);
    }

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }
}
