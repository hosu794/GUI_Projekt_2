import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

public class CalendarControl extends JPanel {
    private JSpinner monthSpinner;
    private JSpinner yearSpinner;
    private JButton[][] dayButtons;

    private LocalDate selectedDate;

    public CalendarControl() {
        selectedDate = LocalDate.now();

        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        monthSpinner = new JSpinner(new SpinnerNumberModel(selectedDate.getMonthValue(), 1, 12, 1));
        monthSpinner.addChangeListener(e -> updateCalendar());
        topPanel.add(monthSpinner);

        yearSpinner = new JSpinner(new SpinnerNumberModel(selectedDate.getYear(), 1900, 2100, 1));
        yearSpinner.addChangeListener(e -> updateCalendar());
        topPanel.add(yearSpinner);

        add(topPanel, BorderLayout.NORTH);

        JPanel calendarPanel = new JPanel(new GridLayout(7, 7));
        dayButtons = new JButton[6][7];

        String[] dayOfWeekNames = {"Pon", "Wt", "Śr", "Czw", "Pt", "Sob", "Nd"};
        for (int i = 0; i < 7; i++) {
            JButton dayOfWeekButton = new JButton(dayOfWeekNames[i]);
            dayOfWeekButton.setEnabled(false);
            calendarPanel.add(dayOfWeekButton);
        }

        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 7; col++) {
                JButton dayButton = new JButton();
                dayButton.addActionListener(e -> {
                    JButton source = (JButton) e.getSource();
                    int dayOfMonth = Integer.parseInt(source.getText());
                    selectedDate = LocalDate.of((int) yearSpinner.getValue(), (int) monthSpinner.getValue(), dayOfMonth);
                });
                dayButtons[row][col] = dayButton;
                calendarPanel.add(dayButton);
            }
        }

        add(calendarPanel, BorderLayout.CENTER);

        updateCalendar();
    }

    private void updateCalendar() {
        int selectedMonth = (int) monthSpinner.getValue();
        int selectedYear = (int) yearSpinner.getValue();

        YearMonth yearMonth = YearMonth.of(selectedYear, selectedMonth);

        int firstDayOfWeek = yearMonth.atDay(1).getDayOfWeek().getValue();
        int lastDayOfMonth = yearMonth.lengthOfMonth();

        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 7; col++) {
                JButton dayButton = dayButtons[row][col];
                dayButton.setText("");

                if (row == 0 && col < firstDayOfWeek) {
                    dayButton.setEnabled(false);
                } else if ((row * 7 + col - firstDayOfWeek + 1) <= lastDayOfMonth) {
                    int dayOfMonth = row * 7 + col - firstDayOfWeek + 1;
                    dayButton.setText(String.valueOf(dayOfMonth));

                    LocalDate date = LocalDate.of(selectedYear, selectedMonth, dayOfMonth);
                    if (date.equals(selectedDate)) {
                        dayButton.setSelected(true);
                    } else {
                        dayButton.setSelected(false);
                    }
                    dayButton.setEnabled(true);
                } else {
                    dayButton.setEnabled(false);
                }
            }
        }
    }

    public void setDate(LocalDate date) {
        selectedDate = date;
        monthSpinner.setValue(selectedDate.getMonthValue());
        yearSpinner.setValue(selectedDate.getYear());

        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 7; col++) {
                JButton dayButton = dayButtons[row][col];
                if (dayButton.getText().isEmpty()) {
                    continue;
                }
                int dayOfMonth = Integer.parseInt(dayButton.getText());
                if (dayOfMonth == selectedDate.getDayOfMonth()) {
                    dayButton.setSelected(true);
                } else {
                    dayButton.setSelected(false);
                }
            }
        }
    }

    public LocalDate getSelectedDate() {
        return selectedDate;
    }
}