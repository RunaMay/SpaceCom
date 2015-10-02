package com.confluence.plugin.roadmap;

import com.atlassian.confluence.content.render.xhtml.ConversionContext;
import com.atlassian.confluence.content.render.xhtml.XhtmlException;
import com.atlassian.confluence.core.ContentEntityObject;
import com.atlassian.confluence.macro.Macro;
import com.atlassian.confluence.macro.MacroExecutionException;
import com.atlassian.confluence.pages.Page;
import com.atlassian.confluence.renderer.PageContext;
import com.atlassian.confluence.renderer.radeox.macros.MacroUtils;
import com.atlassian.confluence.spaces.Space;
import com.atlassian.confluence.user.AuthenticatedUserThreadLocal;
import com.atlassian.confluence.util.velocity.VelocityUtils;
import com.atlassian.confluence.xhtml.api.MacroDefinition;
import com.atlassian.confluence.xhtml.api.MacroDefinitionHandler;
import com.atlassian.confluence.xhtml.api.XhtmlContent;
import com.atlassian.renderer.RenderContext;
import com.atlassian.renderer.v2.RenderMode;
import com.atlassian.renderer.v2.macro.BaseMacro;
import com.atlassian.renderer.v2.macro.MacroException;
import com.atlassian.user.User;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.*;
import org.jfree.util.Log;

import java.lang.reflect.Field;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class ExampleMacro extends BaseMacro
{
    private static final String MACRO_BODY_TEMPLATE = "templates/roadmap-view.vm";


    private final XhtmlContent xhtmlUtils;

    public ExampleMacro(XhtmlContent xhtmlUtils)
    {
        this.xhtmlUtils = xhtmlUtils;
    }

    @Override
    public String execute(Map params, String body, RenderContext renderContext)
            throws MacroException
    {
      
    	Map<String, Object> context = MacroUtils.defaultVelocityContext();
    	PageContext pc = (PageContext) renderContext;
        ContentEntityObject contentObject = pc.getEntity();
        String q = contentObject.getBodyAsString();
       String table = q.split("tbody")[1];
       table = table.replace("&nbsp;", "");
       table = table.replace("/", "");
       table = table.replace(">", "");
       table = table.replace("<", "");

       String[]d = table.split("tr");

List<Project> prList = new ArrayList<Project>();
       for (String str : d) {
    	    if(!str.isEmpty()){
    	    	 if(!str.contains("Name")){
    	   String[]prs = str.split("td");
	    	List<String>pr = new ArrayList<String>();

	    	   for (int i = 1; i < prs.length; i++) {
    		   if(!prs[i].isEmpty()&&prs[i]!=null)
    			   pr.add(prs[i]);
    		   
    	   }
	    	
    	   if(pr.size()>7){
          	List<String> newEst = new ArrayList<String>();
 		   List<String> delay = new ArrayList<String>();

    	   for (int i = 8; i < pr.size(); i++) {
			if(pr.get(i).contains("Reprioritization")||pr.get(i).contains("Resource"))
               	delay.add("blue.png");
           	else if(pr.get(i).contains("Estimate"))
               	delay.add("red.png");
           	else{
				newEst.add(pr.get(i));
           	}
		}
    	   Project p = new Project();
    	   p.setProjectName(pr.get(0));
               p.setProgram(pr.get(1));
               p.setUrl(pr.get(2));
               
               p.setOriginalCompletionEstimate(pr.get(3));
              
               p.setProjectPhase(pr.get(4));
              
               p.setDateToday(pr.get(5));
             
               p.setComplete(pr.get(6));
               p.setPercentageComplete(pr.get(7));//doc.get(8));
               p.setDelayReason(delay);
               p.setNewEstimate(newEst);
    	   prList.add(p);
    	   }
    	    }
	}
       }

       

/*    	Map<String, String> project1 = new HashMap<String, String>();
        Project p1 = new Project("project 1", "Program A", "https://confluence.allegiantair.com:8443/pages/viewpage.action?pageId=12714241",
        		"6/10/2015", "Execution", "8/28/2015", 
        		"YES", "50%", new HashMap<String, String>());
        Project p2 = new Project("project 2", "Program B", "https://confluence.allegiantair.com:8443/pages/viewpage.action?pageId=12714245",
        		"9/1/2015", "Closing", "8/28/2015", 
        		"YES", "20%", new HashMap<String, String>());
        
        Project p3 = new Project("project 3", "Program A", "https://confluence.allegiantair.com:8443/pages/viewpage.action?pageId=12714248",
        		"12/30/2015", "Initiation", "8/28/2015", 
        		"NO", "60%", new HashMap<String, String>());
        Project p4 = new Project("project 4", "Program C", "https://confluence.allegiantair.com:8443/pages/viewpage.action?pageId=12714243",
        		"11/5/2015", "Design", "8/28/2015", 
        		"NO", "10%", new HashMap<String, String>());
        
        Project p5 = new Project("project 5", "Program B", "https://confluence.allegiantair.com:8443/pages/viewpage.action?pageId=12714241",
        		"Undifined", "Undifined", "8/28/2015", 
        		"YES", "100%", new HashMap<String, String>());
        
        */
        
      /*  List<Project> prsList= new ArrayList<Project>();
        prsList.add(p1);
        prsList.add(p2);
        prsList.add(p3);
        prsList.add(p4);
        prsList.add(p5);*/
        
    	List<Project> prsList= new ArrayList<Project>();
    	prsList = prList;//getProjectsListFromExcel();   
        List<String> programsList= new ArrayList<String>();
        
        for (Project project : prsList) {
        	  if (!programsList.contains(project.getProgram()))
        	        programsList.add(project.getProgram());        	        
		}
        
        HashMap<String, List<Project>> projectsByProgram = new HashMap<String, List<Project>>();
       
       for (String prog : programsList) {
    	   List<Project> prs = new ArrayList<Project>();
        for (Project project : prsList) {
        		if (prog.equalsIgnoreCase(project.getProgram())){
        			//projectsByProgram.put(prog, project);
        			prs.add(project);
        		}
		}
        if(prs!=null&&prs.size()>0)
        	projectsByProgram.put(prog, prs);
       }
        
   /*     
        project1.put("projectName","project 1");
        project1.put("program","Program A");
        project1.put("url","https://confluence.allegiantair.com:8443/pages/viewpage.action?pageId=12714241");
        project1.put("originalCompletionEstimate","6/10/2015");
        project1.put("projectPhase","Execution");
        project1.put("dateToday","8/28/2015");
        project1.put("complete","YES");
        project1.put("percentageComplete","50%");
        project1.put("newCEstimate1","7/1/2015");
        project1.put( "delayReason","Poor Estimate");
        
        Map<String, String> project2 = new HashMap<String, String>();
        
        project2.put("projectName",p2.getProjectName());
        project2.put("program",p2.getProgram());
        project2.put("url",p2.getUrl());
        project2.put("originalCompletionEstimate",p2.getOriginalCompletionEstimate());
        project2.put("projectPhase",p2.getProjectPhase());
        project2.put("dateToday",p2.getDateToday());
        project2.put("complete",p2.getComplete());
        project2.put("percentageComplete",p2.getPercentageComplete());
        project2.put("newCEstimate1",p2.getNewCEstimate1());
        project2.put( "delayReason",p2.getDelayReason());
        */
       context.put("programsList", programsList);
       context.put("projectsByProgram", projectsByProgram);

        
        
        // get all spaces in this installation

        // render the Velocity template with the assembled context
        return VelocityUtils.getRenderedTemplate(MACRO_BODY_TEMPLATE, context);


       // return builder.toString();
        
    }

	@Override
	public RenderMode getBodyRenderMode() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasBody() {
		// TODO Auto-generated method stub
		return false;
	}
    
    private static List getProjectsListFromExcel() {
        List<Project> prsList= new ArrayList<Project>();
       try {
     	   FileInputStream file = new FileInputStream(new File("C:\\ChartMacroDataFile2.xls"));

            // Using XSSF for xlsx format, for xls use HSSF
    	    HSSFWorkbook workbook = new HSSFWorkbook(file);
    	  // XSSFWorkbook workbook = new XSSFWorkbook(file);
            Sheet sheet = workbook.getSheetAt(0);
            
            Iterator rowIterator = sheet.iterator();
            while (rowIterator.hasNext()) {

                //Student student = new Student();
                Row row = (Row) rowIterator.next();

                Iterator cellIterator = row.cellIterator();

                HashMap<Integer, String> doc = new HashMap<Integer, String>();
                HashMap<Integer, Integer> numbers = new HashMap<Integer, Integer>();

int num = 0;
                //Iterating over each cell (column wise)  in a particular row.
                while (cellIterator.hasNext()) {
num++;
                    Cell cell = (Cell) cellIterator.next();
                    if (Cell.CELL_TYPE_STRING == cell.getCellType()) {
                    	   if (cell.getStringCellValue()!=null)
                        doc.put(num,cell.getStringCellValue());
                    	   else
                               doc.put(num,"");


                    	
                   } else if (Cell.CELL_TYPE_NUMERIC == cell.getCellType()) {
                	   doc.put(num, Double.toString(cell.getNumericCellValue()));
                	   
                    	                        }
                  
              
                }
                Project p = new Project();
                if(doc.get(1)!=null)
                p.setProjectName(doc.get(1));
                if(doc.get(2)!=null)
                p.setProgram(doc.get(2));
                if(doc.get(3)!=null)
                p.setUrl(doc.get(3));
                if(doc.get(4)!=null)
                p.setOriginalCompletionEstimate(doc.get(4));
                if(doc.get(5)!=null)
                p.setProjectPhase(doc.get(5));
                if(doc.get(6)!=null)
                p.setDateToday(doc.get(6));
                if(doc.get(7)!=null)
                p.setComplete(doc.get(7));
                if(doc.get(8)!=null&&!doc.get(8).contains("Percentage"))
                p.setPercentageComplete(Double.toString(Double.parseDouble(doc.get(8))*1000));//doc.get(8));
                
                List<String> newEst = new ArrayList<String>();
                List<String> delay = new ArrayList<String>();

         /*  
    		
                for (int i = 9; i < doc.size()-2; i=2+i)
                {++n;
                if(doc.get(i)!=null){
                	newEst.put(n,doc.get(i));
                }
                }               
                n=0;*/
            	int n = 0;
                for (int i = 8; i < doc.size(); ++i)
                {
                if(doc.get(i)!=null){
                	if(doc.get(i).contains("Reprioritization")||doc.get(i).contains("Resource"))
                    	delay.add("blue.png");
                	else if(doc.get(i).contains("Estimate"))
                    	delay.add("red.png");
                	else{
                    	newEst.add(doc.get(i));
                	}

                }
                }               
                p.setNewEstimate(newEst);
                p.setDelayReason(delay);
                prsList.add(p);

                
                
                
            }
            


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return prsList;
    }

}