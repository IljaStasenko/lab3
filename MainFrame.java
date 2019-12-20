package lab3;

import com.opencsv.CSVWriter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;



public class MainFrame extends JFrame {

    private static final int width = 1200;
    private static final int height = 500;

    private Double[] coeff;

    private JMenuItem saveToTextMenuItem;
    private JMenuItem saveToGraphicsMenuItem;
    private JMenuItem searchValueMenuItem;
    private JMenuItem informationItem;
    private JMenuItem searchFromToItem;
    private JMenuItem commaSeparatedValues;

    private JTextField from_field;
    private JTextField to_field;
    private JTextField step_field;
    private Box BoxResult;

    private GTCell cell = new GTCell();

    private GornerTable data;
    private JFileChooser fileChooser = null;

    private DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance();


    public MainFrame(Double[] coeff) {
        super("Табулирование многочлена на отрезке по схеме Горнера");
        this.coeff = coeff;
        setSize(width, height);
        Toolkit kit = Toolkit.getDefaultToolkit();
        setLocation((kit.getScreenSize().width - width) / 2,
                (kit.getScreenSize().height - height) / 2);

        JMenuBar menu = new JMenuBar();
        setJMenuBar(menu);
        JMenu fileMenu = new JMenu("Файл");
        menu.add(fileMenu);
        JMenu tableMenu = new JMenu("Таблица");
        menu.add(tableMenu);
        JMenu spravkaMenu = new JMenu("Справка");
        menu.add(spravkaMenu);

        Action saveToTextAction = new AbstractAction("Сохранить в текстовый файл") {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (fileChooser == null) {
                    fileChooser = new JFileChooser();
                    fileChooser.setCurrentDirectory(new File("."));
                }
                if(fileChooser.showSaveDialog(MainFrame.this)==JFileChooser.APPROVE_OPTION)
                   saveToTextFile(fileChooser.getSelectedFile());
            }
        };
        saveToTextMenuItem = fileMenu.add(saveToTextAction);
        saveToTextMenuItem.setEnabled(true);

        Action saveToGraphicsAction = new AbstractAction("Сохранить данные для построения графика") {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (fileChooser == null) {
                    fileChooser = new JFileChooser();
                    fileChooser.setCurrentDirectory(new File("."));
                }
                 if(fileChooser.showSaveDialog(MainFrame.this)==JFileChooser.APPROVE_OPTION)
                   saveToGraphicsFile(fileChooser.getSelectedFile());
            }
        };
        saveToGraphicsMenuItem = fileMenu.add(saveToGraphicsAction);
        saveToGraphicsMenuItem.setEnabled(false);

        Action saveToCVSAction = new AbstractAction("Сохранить данные в  CSV-файл")
        {
            public void actionPerformed(ActionEvent arg0) {
                if (fileChooser == null){
                    fileChooser = new JFileChooser();
                    fileChooser.setCurrentDirectory(new File("."));
                }
                if (fileChooser.showSaveDialog(MainFrame.this) == JFileChooser.APPROVE_OPTION){
                    saveToCSVFile(fileChooser.getSelectedFile());
                }
            }
        };

        commaSeparatedValues = fileMenu.add(saveToCVSAction);
        commaSeparatedValues.setEnabled(true);

        Action searchValueAction = new AbstractAction("Найти значение многочлена") {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String value = JOptionPane.showInputDialog(MainFrame.this,
                        "Введите значение для поиска", "Поиск значения",
                        JOptionPane.QUESTION_MESSAGE);
                cell.setSearch(value);
                getContentPane().repaint();
            }
        };

        Action searchFromToAction = new AbstractAction("Найти из диапазона") {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Box diapazonBox=Box.createVerticalBox();
                JLabel input = new JLabel("Введите границы диапазона");
                JLabel from = new JLabel("от:");
                JLabel to = new JLabel("до");
                JTextField searchFrom = new JTextField("0.0",10);
                searchFrom.setMaximumSize(searchFrom.getPreferredSize());
                JTextField searchTo = new JTextField("0.0",10);
                searchFrom.setMaximumSize(searchFrom.getPreferredSize());
                diapazonBox.add(Box.createVerticalGlue());
                diapazonBox.add(input);
                diapazonBox.add(Box.createVerticalStrut(20));
                Box fromBox = Box.createHorizontalBox();
                fromBox.add(from);
                fromBox.add(Box.createHorizontalStrut(10));
                fromBox.add(searchFrom);
                diapazonBox.add(fromBox);
                diapazonBox.add(Box.createVerticalStrut(20));
                Box toBox = Box.createHorizontalBox();
                toBox.add(to);
                toBox.add(Box.createHorizontalStrut(10));
                toBox.add(searchTo);
                diapazonBox.add(toBox);
                diapazonBox.add(Box.createVerticalGlue());
                JOptionPane.showMessageDialog(MainFrame.this,
                        diapazonBox, "" +
                                "Найти из диапазона", JOptionPane.QUESTION_MESSAGE);
                cell.setdiap(searchFrom.getText(),searchTo.getText());
                getContentPane().repaint();
            }
        };
        searchValueMenuItem = tableMenu.add(searchValueAction);
        searchValueMenuItem.setEnabled(false);
        searchFromToItem=tableMenu.add(searchFromToAction);
        searchFromToItem.setEnabled(true);

        Action aboutProgramAction=new AbstractAction("О программе") {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Box information=Box.createVerticalBox();
                JLabel author = new JLabel("Автор: Стасенко Илья");
                JLabel group = new JLabel("Студент 8 группы, 2 курс");
                JLabel image=new JLabel(new ImageIcon(MainFrame2.class.getResource("STD.jpg")));
                information.add(Box.createVerticalGlue());
                information.add(author);
                information.add(Box.createVerticalStrut(10));
                information.add(group);
                information.add(Box.createVerticalStrut(10));
                information.add(image);
                information.add(Box.createVerticalStrut(10));
                information.add(Box.createVerticalGlue());

                JOptionPane.showMessageDialog(MainFrame.this,
                        information, "" +
                                "О программе", JOptionPane.INFORMATION_MESSAGE);

            }
        };
        informationItem=spravkaMenu.add(aboutProgramAction);
        informationItem.setEnabled(true);

        JButton calculateButton = new JButton("Вычислить");
        calculateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    // Считать значения границ отрезка, шага из полей ввода
                    Double from = Double.parseDouble(from_field.getText());
                    Double to = Double.parseDouble(to_field.getText());
                    Double step = Double.parseDouble(step_field.getText());
                    if((from<to && step>0.0)||(from>to && step<0.0)) {

                        // На основе считанных данных создать модель таблицы
                        data = new GornerTable(from, to, step, MainFrame.this.coeff);
                        // Создать новый экземпляр таблицы
                        JTable table = new JTable(data);
                        // Установить в качестве визуализатора ячеек для класса Double разработанный визуализатор
                        table.setDefaultRenderer(Double.class, cell);
                        // Установить размер строки таблицы в 30 пикселов
                        table.setRowHeight(30);
                        // Удалить все вложенные элементы из контейнера hBoxResult
                        BoxResult.removeAll();
                        // Добавить в hBoxResult таблицу, "обѐрнутую" в панель с полосами прокрутки
                        BoxResult.add(new JScrollPane(table));
                        // Обновить область содержания главного окна
                        getContentPane().validate();
                        //  Пометить ряд элементов меню как доступных saveToTextMenuItem.setEnabled(true);
                        saveToGraphicsMenuItem.setEnabled(true);
                        searchValueMenuItem.setEnabled(true);
                    }
                    else
                    {
                        JOptionPane.showMessageDialog(MainFrame.this,
                                "Проверьте введенные данные\nНевозможно провести вычисления", "" +
                                        "Ошибка ввода", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException ex) {
                    // В случае ошибки преобразования показать сообщение об ошибке
                    JOptionPane.showMessageDialog(MainFrame.this, "Ошибка в формате записи числа с плавающей точкой",
                            "Ошибочный формат числа", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        JButton resetButton = new JButton("Очистить поля");
        // Задать действие на нажатие "Очистить поля" и привязать к кнопке
        resetButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                // Установить в полях ввода значения по умолчанию
                from_field.setText("0.0");
                to_field.setText("1.0");
                step_field.setText("0.1");
                // Удалить все вложенные элементы контейнера BoxResult
                BoxResult.removeAll();
                // Добавить в контейнер пустую панель
                BoxResult.add(new JPanel());
                // Пометить элементы меню как недоступные
                saveToTextMenuItem.setEnabled(false);
                saveToGraphicsMenuItem.setEnabled(false);
                searchValueMenuItem.setEnabled(false);
                // Обновить область содержания главного окна
                getContentPane().validate();
            }
        });

        Box dataBox= Box.createHorizontalBox();
        dataBox.add(Box.createHorizontalGlue());
        JLabel from_label=new JLabel("х изменяется на интервале от:");
        dataBox.add(from_label);
        dataBox.add(Box.createHorizontalStrut(10));
        from_field = new JTextField("0.0",10);
        from_field.setMaximumSize(from_field.getPreferredSize());
        dataBox.add(from_field);
        dataBox.add(Box.createHorizontalStrut(20));
        JLabel to_label=new JLabel("до:");
        dataBox.add(to_label);
        dataBox.add(Box.createHorizontalStrut(10));
        to_field = new JTextField("0.0",10);
        to_field.setMaximumSize(to_field.getPreferredSize());
        dataBox.add(to_field);
        dataBox.add(Box.createHorizontalStrut(20));
        JLabel step_label=new JLabel("c шагом:");
        dataBox.add(step_label);
        dataBox.add(Box.createHorizontalStrut(10));
        step_field = new JTextField("0.0",10);
        step_field.setMaximumSize(step_field.getPreferredSize());
        dataBox.add(step_field);
        dataBox.add(Box.createHorizontalGlue());
        dataBox.setPreferredSize(new
                Dimension(new Double(dataBox.getMaximumSize().getWidth()).intValue(), new
                Double(dataBox.getMinimumSize().getHeight()).intValue()*2));
        getContentPane().add(dataBox, BorderLayout.NORTH);

        Box buttonBox=Box.createHorizontalBox();
        buttonBox.add(Box.createHorizontalGlue());
        buttonBox.add(calculateButton);
        buttonBox.add(Box.createHorizontalStrut(20));
        buttonBox.add(resetButton);
        buttonBox.add(Box.createHorizontalGlue());
        buttonBox.setPreferredSize(new
                Dimension(new Double(buttonBox.getMaximumSize().getWidth()).intValue(), new
                Double(buttonBox.getMinimumSize().getHeight()).intValue()*2));
        getContentPane().add(buttonBox, BorderLayout.SOUTH);

        BoxResult=Box.createHorizontalBox();
        BoxResult.add(new JPanel());
        getContentPane().add(BoxResult, BorderLayout.CENTER);

    }

    protected void saveToGraphicsFile(File selectedFile) {
        try {
            // Создать новый байтовый поток вывода, направленный в указанный файл
            DataOutputStream out = new DataOutputStream(new FileOutputStream(selectedFile));
            // Записать в поток вывода попарно значение X в точке, значение многочлена в точке
            for (int i = 0; i < data.getRowCount(); i++) {
                out.writeDouble((Double) data.getValueAt(i, 0));
                out.writeDouble((Double) data.getValueAt(i, 1));
            } // Закрыть поток вывода
            out.close();
        } catch (Exception e) {
            // Исключительную ситуацию "ФайлНеНайден" в данном случае можно не обрабатывать, так как мы файл создаѐм, а не открываем для чтения
        }
    }

    protected void saveToTextFile(File selectedFile) {
        try {
            // Создать новый символьный поток вывода, направленный в указанный файл
            PrintStream out = new PrintStream(selectedFile);
            // Записать в поток вывода заголовочные сведения
            out.println("Результаты табулирования многочлена по схеме Горнера");
            out.print("Многочлен: ");
            for (int i = 0; i < coeff.length; i++) {
                out.print(coeff[i] + "*X^" + (coeff.length - i - 1));
                if (i != coeff.length - 1)
                    out.print(" + ");
            }
            out.println("");
            out.println("Интервал от " + data.getFrom() + " до " + data.getTo() + " с шагом " + data.getStep());
            out.println("====================================================");
            // Записать в поток вывода значения в точках
            for (int i = 0; i < data.getRowCount(); i++) {
                out.println("Значение в точке " + data.getValueAt(i, 0) + " равно " + data.getValueAt(i, 1));
            }
            // Закрыть поток
            out.close();
        } catch (FileNotFoundException e) {
            // Исключительную ситуацию "ФайлНеНайден" можно не // обрабатывать, так как мы файл создаѐм, а не открываем
        }
    }

    protected void saveToCSVFile(File selectedFile)
    {
        try{
            CSVWriter writer = new CSVWriter(new FileWriter(selectedFile));
            String [] record = "Результаты табулирования многочлена по схеме Горнера".split(",");
            writer.writeNext(record);
            String record1 = "Интервал от " + data.getFrom() + " до " + data.getTo() + " с шагом " +  data.getStep();
            record=record1.split(",");
            writer.writeNext(record);
            String record2;
            record1="";
            record2="";
            for (int i = 0; i < data.getRowCount(); i++){

                record1 = formatter.format(data.getValueAt(i, 0)) + ";" + formatter.format(data.getValueAt(i, 1)) + ";";

                record = record1.split(";");
                writer.writeNext(record, true);
                record1="";
            }
            writer.close();
        }catch(Exception e){

        }
    }
}

