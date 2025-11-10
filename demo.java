
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class demo{
    //MOT
    static Map<String,String> IS= new HashMap<String,String>();
    static Map<String,String> AD= new HashMap<String,String>();
    static Map<String,String> DL= new HashMap<String,String>();
    static Map<String,String> RG= new HashMap<String,String>();
    //Symbol table
    static List<String> symtab= new ArrayList<String>();
    static Map<String,Integer>symtabaddr=new HashMap<String,Integer>();
    //Literal table
    static List<String> littab= new ArrayList<String>();
    static Map<String,Integer>littabaddr=new HashMap<String,Integer>();
    //Intermediate code
    static List<String> intermediate= new ArrayList<String>();
    public static void main(String[] args) {
        IS.put("ADD","01");
        IS.put("SUB","02");
        IS.put("MULT","03");
        IS.put("MOVER","04");
        IS.put("MOVEM","05");
        IS.put("DIV","06");
        AD.put("START","01");
        AD.put("END","02");
        DL.put("DS","01");
        DL.put("DC","02");
        RG.put("AREG","01");
        RG.put("BREG","02");
        RG.put("CREG","03");
        RG.put("DREG","04");
        String program[]={
            "START 100",
            "ADD AREG,X",
            "SUB BREG,Y",
            "MOVEM CREG,=3",
            "X DC 2",
            "Y DS 1",
            "END"
        };
        pass1(program);
        System.out.println("Symbol Table");
        for(int i=0;i<symtab.size();i++){
            String sym=symtab.get(i);
            System.out.println(""+i+" "+sym+" "+symtabaddr.get(sym));
        }
        System.out.println("\n Literal table");
        for(int i=0;i<littab.size();i++){
            String lit=littab.get(i);
            System.out.println(""+i+" "+lit+" "+littabaddr.get(lit));
        }
        System.out.println("\n Intermediate code");
        for(String line:intermediate){
            System.out.println(line);
        }
    }
    static void pass1(String[] program){
        int lc=0;
        for(String line: program){
            String parts[]=line.split("[ ,]+");

            if(AD.containsKey(parts[0])){
                if(parts[0].equals("START")){
                    lc=Integer.parseInt(parts[1]);
                    intermediate.add("AD " + AD.get("START")+ " C "+ lc);
                }else if (parts[0].equals("END")) {
                    intermediate.add("AD "+ AD.get("END"));
                    //assign addresses to literals;
                    for(int i=0;i<littab.size();i++){
                        String lit=littab.get(i);
                        littabaddr.put(lit,lc);
                        intermediate.add("DL "+ DL.get("DC")+ " C ");
                        lc++;
                    }
                }
            }else if(IS.containsKey(parts[0])){
                String ic="IS " + IS.get(parts[0]);
                if(parts.length>1 && RG.containsKey(parts[1])){
                    ic +=" REG " + RG.get(parts[1]);
                }
                if(parts.length>2){
                    if(parts[2].startsWith("=")){
                        if(!littab.contains(parts[2])){
                            littab.add(parts[2]);
                        }
                        int idx=littab.indexOf(parts[2]);
                        ic +=" L "+ idx;
                    }else{
                        //symbol
                        if(!symtab.contains(parts[2])){
                            symtab.add(parts[2]);
                        }
                        int idx=symtab.indexOf(parts[2]);
                        ic +=" S "+ idx;
                    }
                }
                intermediate.add(ic);
                lc++;
            }else{
                //X DC 2;
                String label=parts[0];
                if(!symtab.contains(label)){
                    symtab.add(label);
                }
                int idx=symtab.indexOf(label);
                symtabaddr.put(label,lc);

                //X DC 2
                if(DL.containsKey(parts[1])){
                    if(parts[1].equals("DS")){
                        intermediate.add("DL "+ DL.get(parts[1])+ " C "+ parts[2]);
                        lc+=Integer.parseInt(parts[2]);
                    }else if(parts[1].equals("DC")){
                        intermediate.add("DC "+ DL.get(parts[1])+ " C "+parts[2]);
                    }
                    lc++;
                }
            }
        }
    }

    private static String Substring(int i, int i0) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
