package graph;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSetMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import operations.Constants;
import java.sql.Statement;
import javax.swing.JOptionPane;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

public class BarChart_State extends ApplicationFrame
{
    String tablename="";
    double avgval1[];
    String applicationTitle;
	public BarChart_State(String applicationTitle, String chartTitle, String tablename)
	{
		super(applicationTitle);
                
                this.tablename = tablename;
                this.applicationTitle = applicationTitle;
                this.setVisible(true);
                 Connection connection = null;
        ResultSet resultSet = null;
        
        Statement stmt = null;
        PreparedStatement statement = null;
        try{
             Class.forName("com.mysql.jdbc.Driver");
           connection = DriverManager.getConnection(Constants.DBURL,Constants.USERNAME,Constants.PASSWORD);

}
catch(          ClassNotFoundException | SQLException e){
JOptionPane.showMessageDialog(null, e.getMessage());
}
		JFreeChart barChart = ChartFactory.createBarChart(chartTitle, "", "", createDataset(resultSet), PlotOrientation.VERTICAL, true, true, false);

		ChartPanel chartPanel = new ChartPanel(barChart);
		chartPanel.setPreferredSize(new java.awt.Dimension(800, 600));
		setContentPane(chartPanel);
		this.pack();
                this.setLocationRelativeTo(this);
	}

	private CategoryDataset createDataset(ResultSet resultSet)
	{
           final DefaultCategoryDataset dataset = new DefaultCategoryDataset();
            Connection connection = null;
            ResultSet resultSet1 = null;
            double total=0.0;
            double avgval[];
        
        Statement stmt = null;
        resultSet = null;
        PreparedStatement statement = null;
		try
                {
                    Class.forName("com.mysql.jdbc.Driver");
           connection = DriverManager.getConnection(Constants.DBURL,Constants.USERNAME,Constants.PASSWORD);
String sql="select * from "+tablename;

statement=connection.prepareStatement(sql);
resultSet=statement.executeQuery();
//System.out.println(resultSet.getFetchSize());

            ResultSetMetaData rsmd = resultSet.getMetaData();
            int colcount=rsmd.getColumnCount();
                String colnames[] = new String[colcount-1];
                avgval = new double[colcount-1];
                avgval1 = new double[colcount-1];
                resultSet=null;
                
                String speed="";
                for(int i=2;i<=colcount;i++)
                {
                colnames[i-2] = rsmd.getColumnName(i);
                System.out.println(colnames[i-2]);
sql="select SUM("+colnames[i-2]+") from "+tablename;
statement=connection.prepareStatement(sql);
resultSet=statement.executeQuery();
if(resultSet.next())
{
avgval[i-2] = resultSet.getDouble(1);
total=total+avgval[i-2];
}


}
                for(int i = 2;i<=colcount;i++){
                    String sql1="select "+colnames[i-2]+" from "+tablename+" where State = "+ "'"+applicationTitle+"';";
System.out.println(sql1);
resultSet1=statement.executeQuery(sql1);
if(resultSet1.next())
{
avgval1[i-2] = resultSet1.getDouble(1);
System.out.println(avgval1[i-2]);
}
                }
for(int i=2;i<=colcount;i++)
{
    double temp = avgval1[i-2]/total * 100;
    dataset.addValue(temp, colnames[i-2], speed);               
}
		
                }
                catch(ClassNotFoundException | SQLException e)
                {
                System.out.println(e.getMessage());
                }
                
		return dataset;
	}
}