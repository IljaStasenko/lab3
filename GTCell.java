package lab3;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;


public class GTCell implements TableCellRenderer{

    private String search=null;
    private String searchFrom=null;
    private String searchTo=null;
    private JPanel panel=new JPanel();
    private JLabel label=new JLabel();
    private DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance();

    public GTCell(){
        formatter.setMaximumFractionDigits(5);
        formatter.setGroupingUsed(false);
        DecimalFormatSymbols dottedDouble = formatter.getDecimalFormatSymbols();
        dottedDouble.setDecimalSeparator('.');
        formatter.setDecimalFormatSymbols(dottedDouble);
        panel.add(label);
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));
    }
    @Override
    public Component getTableCellRendererComponent(JTable jTable, Object value, boolean b, boolean b1, int row, int col) {
        String formattedDouble = formatter.format(value);
        label.setText(formattedDouble);
        if((col==1||col==2) && search!=null && search.equals(formattedDouble)){
            panel.setBackground(Color.RED);
            label.setForeground(Color.BLACK);
        }
        else if(((col==1 || col==3) && row%2!=0) || ((col==0|| col==2) && row%2==0)){
            panel.setBackground(Color.WHITE);
            label.setForeground(Color.BLACK);
        }
        else {
            panel.setBackground(Color.BLACK);
            label.setForeground(Color.WHITE);
        }

       if(searchFrom!=null && searchTo!=null &&         // диапазон
                Double.parseDouble(formattedDouble) >= Double.parseDouble(searchFrom) &&
                Double.parseDouble(formattedDouble) <= Double.parseDouble(searchTo)) {
           panel.setBackground(Color.BLUE);
           if(((col==1 || col==3) && row%2!=0) || ((col==0|| col==2) && row%2==0))
               label.setForeground(Color.BLACK);
           else
               label.setForeground(Color.WHITE);
       }
        return panel;
    }

    public void setSearch(String search) {
        this.search = search;
    }
    public void setdiap(String searchFrom, String searchTo) {
        this.searchFrom = searchFrom;
        this.searchTo = searchTo;
    }
}
