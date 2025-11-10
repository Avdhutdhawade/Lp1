
import java.util.*;
public class MacroPass1 {
 static Map<String, Integer> MNT = new HashMap<>(); // Macro Name Table
 static List<String> MDT = new ArrayList<>(); // Macro Definition Table
 static List<String> intermediateCode = new ArrayList<>(); // Lines outside macros
 public static void main(String[] args) {
 List<String> sourceCode = List.of(
 "MACRO",
 "INCR &ARG",
 "A = A + &ARG",
 "MEND",
 "MACRO",
 "DECR &ARG",
 "A = A - &ARG",
 "MEND",
 "START",
 "INCR 5",
 "DECR 2",
 "END"
 );
 performPass1(sourceCode);
 System.out.println("----- PASS 1 OUTPUT -----");
 System.out.println("MNT (Macro Name Table):");
 MNT.forEach((name, index) -> System.out.println(name + " -> MDT[" + index + "]"));
 System.out.println("\nMDT (Macro Definition Table):");
 for (int i = 0; i < MDT.size(); i++) {
 System.out.println(i + ": " + MDT.get(i));
 }
 System.out.println("\nIntermediate Code (Non-macro lines):");
 for (String line : intermediateCode) {
 System.out.println(line);
 }
 }
 static void performPass1(List<String> source) {
 boolean insideMacro = false;
 int mdtPointer = 0;
 for (int i = 0; i < source.size(); i++) {
 String line = source.get(i).trim();
 if (line.equals("MACRO")) {
 insideMacro = true;
 continue;
 }
 if (insideMacro) {
 if (line.equals("MEND")) {
 MDT.add("MEND");
 insideMacro = false;
 } else {
 if (MDT.isEmpty() || MDT.get(MDT.size() - 1).equals("MEND")) {
 // First line of macro header (macro name and parameters)
 String[] parts = line.split(" ");
 String macroName = parts[0];
 MNT.put(macroName, mdtPointer);
 }
 MDT.add(line);
 mdtPointer++;
 }
 } else {
 intermediateCode.add(line); // Code outside macro definitions
 }
 }
 }
}
