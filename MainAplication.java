package lab3;

import javax.swing.*;
import java.awt.*;

public class MainAplication {
    public static void main(String[] args)
    {
        /*JFrame frame=new JFrame();
        int width=500;
        int height=500;
        frame.setSize(width,height);
        frame.setTitle("Окошко");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        Toolkit kit = Toolkit.getDefaultToolkit();
        frame.setLocation((kit.getScreenSize().width-width)/2,
                (kit.getScreenSize().height-height)/2);
        JMenuBar menuBar=new JMenuBar();
        JMenu file=new JMenu("Файл");
        JMenu podmenu=new JMenu("Блаблабла");
        file.add(podmenu);
        menuBar.add(file);
        frame.setJMenuBar(menuBar);
        JTable table=new JTable();
        table.setSize(10,10);
        frame.add(new JScrollPane(table),BorderLayout.CENTER);
        frame.add(menuBar,BorderLayout.WEST);*/
        if (args.length==0) { System.out.println("Невозможно табулировать многочлен, для которого не задано ни одного коэффициента!");
            System.exit(-1);
        }
        // Зарезервировать места в массиве коэффициентов столько, сколько аргументов командной строки
        Double[] coeff = new Double[args.length];
        int i = 0;
        try {
            // Перебрать все аргументы, пытаясь преобразовать их в Double
            for (String arg: args) {
                coeff[i++] = Double.parseDouble(arg);
            }
        } catch (NumberFormatException ex) {
            // Если преобразование невозможно - сообщить об ошибке и завершиться
            System.out.println("Ошибка преобразования строки '" + args[i] + "' в число типа Double");
            System.exit(-2);
        }

        MainFrame frame=new MainFrame(coeff);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

    }

}
