/* “ On my honor, I have neither given nor
received unauthorized aid on this assignment ”. Ankit Aggarwal UFID -7135 6343*/
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class MIPSsim { 
	public static ArrayList<String> inputData = new ArrayList<String>();
	public static HashMap<Integer,Object> objectMap = new HashMap<Integer,Object>();
	public static HashMap<Integer,String> disassemblerMap = new HashMap<Integer,String>();
	public static HashMap<Integer,Integer> dataMap =new HashMap<Integer,Integer>();
	public static void main(String[] args) throws InterruptedException, IOException {
		if (args.length == 0){ 
			System.out.println("Input not provided correctly");
			return;
		}
		File filename = new File(args[0]);
		BufferedReader reader = new BufferedReader(new FileReader(filename));
		String line;
		try
		{
		while ((line = reader.readLine())!=null)
			if (!line.equals(""))
			{
				inputData.add(line);
			}
		}
		catch(FileNotFoundException ex)
        {
            System.out.println("File '" + filename + "' not found :Unable to open File");                
        }
     catch(IOException ex) 
        {
            System.out.println("Error reading file '" + filename + "'");                  
        }
		File file1 = new File("disassembly.txt");
		BufferedWriter output = new BufferedWriter(new FileWriter(file1));
		int start =256;
		boolean br = false;
		int data_index =0;
		for(int i=0;i< inputData.size();i++)
		{
			String str =inputData.get(i);
			if(!br)
			{
			if(str.charAt(0)=='0'&& str.charAt(1)=='1')
			{
				CATa cata = new CATa(str);
				output.write(str+"\t"+start+"\t"+cata.getDisassembler());
				objectMap.put(start, cata);
				disassemblerMap.put(start, cata.getDisassembler());
				if(i!=inputData.size()-1)
				{
					output.write("\n");
				}
				start+= 4;
				if(cata.getDisassembler()=="BREAK")
				{
					br =true;
				}
				
			}
			else if(str.charAt(0)=='1'&& str.charAt(1)=='1')
			{
				if(str.charAt(2)=='0')
				{
					CATba catba = new CATba(str);
					output.write(str+"\t"+start+"\t"+catba.getDisassembler());
					objectMap.put(start, catba);
					disassemblerMap.put(start, catba.getDisassembler());
					if(i!=inputData.size()-1)
					{
						output.write("\n");
					}
					start+= 4;
				}
				else if(str.charAt(2)=='1')
				{
					CATbb catbb = new CATbb(str);
					output.write(str+"\t"+start+"\t"+catbb.getDisassembler());
					objectMap.put(start, catbb);
					disassemblerMap.put(start, catbb.getDisassembler());
					if(i!=inputData.size()-1)
					{
						output.write("\n");
					}
					start+= 4;
				}
			}
			}
			else
			{
				
				output.write(str+ "\t"+start+"\t"+Complement(str));
				if(dataMap.isEmpty())
				{
					data_index =start;
				}
				dataMap.put(start,Complement(str));
				if(i!=inputData.size()-1)
				{
					output.write("\n");
				}
				start+= 4;
			}
		}
		output.close();
		
		//<<<<<<<<<<<<<<<<<<<<<Task 1 Completed>>>>>>>>>>>>>>>>>>>>>
		
		File file2 = new File("simulation.txt"); 
		BufferedWriter output2 = new BufferedWriter(new FileWriter(file2));
		boolean cycle = true;
		start = 256;
		int[] regArr = new int[32];
		int Counter =1;
		Arrays.fill(regArr,0);
		while(cycle)
		{
			Object obj= objectMap.get(start);
			if(obj==null)
			{
				cycle=false;
			}
			else if(obj instanceof CATba)
			{
				CATba catba =(CATba) obj;
				
				switch(catba.getIdentification())
				{
				case "0000":
				{
					//idenCode ="ADD";
					regArr[Integer.parseInt(catba.getRD(),2)]=regArr[Integer.parseInt(catba.getRS(),2)] + regArr[Integer.parseInt(catba.getRT(),2)];
					
				}
					break;
				case "0001":
				{
					//idenCode ="SUB";
					regArr[Integer.parseInt(catba.getRD(),2)]=regArr[Integer.parseInt(catba.getRS(),2)] - regArr[Integer.parseInt(catba.getRT(),2)];
					
				}
					break; 
				case "0010":
				{
					//idenCode ="MUL";
					regArr[Integer.parseInt(catba.getRD(),2)]=regArr[Integer.parseInt(catba.getRS(),2)] * regArr[Integer.parseInt(catba.getRT(),2)];
					
				}
					break;
				case "0011":
				{
					//idenCode ="AND";
					regArr[Integer.parseInt(catba.getRD(),2)]=regArr[Integer.parseInt(catba.getRS(),2)] & regArr[Integer.parseInt(catba.getRT(),2)];
					
				}
					break;
				case "0100":
				{
					//idenCode ="OR";
					regArr[Integer.parseInt(catba.getRD(),2)]=regArr[Integer.parseInt(catba.getRS(),2)] | regArr[Integer.parseInt(catba.getRT(),2)];
					
				}
					break;
				case "0101":
				{
					//idenCode ="XOR";
					regArr[Integer.parseInt(catba.getRD(),2)]=regArr[Integer.parseInt(catba.getRS(),2)] ^ regArr[Integer.parseInt(catba.getRT(),2)];
					
				}
					break;
				case "0110":
				{	//NOR
					regArr[Integer.parseInt(catba.getRD(),2)]=regArr[Integer.parseInt(catba.getRS(),2)] | Integer.parseInt(catba.getRT(),2);
					regArr[Integer.parseInt(catba.getRD(),2)] = ~regArr[Integer.parseInt(catba.getRD(),2)];
					
				}
					break;
				case "0111":
				{
					//idenCode ="SLT";
					if(regArr[Integer.parseInt(catba.getRS(),2)] < regArr[Integer.parseInt(catba.getRT(),2)])
					{
						regArr[Integer.parseInt(catba.getRD(),2)] = 1;
					}
					else
					{
						regArr[Integer.parseInt(catba.getRD(),2)] = 0;
					}
				}
					
				}
				output2.write("--------------------\n");
				output2.write("Cycle:"+Counter+"\t"+start+"\t"+catba.getDisassembler()+"\n\n");
				output2.write("Registers");
				for(int i=0;i<32;i++)
				{
					if(i==0)
					{
						output2.write("\n"+"R00:");
					}
					else if(i==8)
					{
						output2.write("\n"+"R08:");
					}
					else if(i==16)
					{
						output2.write("\n"+"R16:");
					}
					else if(i==24)
					{
						output2.write("\n"+"R24:");
					}
						output2.write("\t"+regArr[i]);
				}
				output2.write("\n\nData");
				int di =0;
				int dix=data_index;
				while(dataMap.containsKey(dix))
				{
					if(di%8==0)
					{
						output2.write("\n"+dix+":");
					}
					output2.write("\t"+dataMap.get(dix));
					di++;
					dix+=4;
				}
				output2.write("\n\n");
				
				
			}
			else if(obj instanceof CATbb)
			{
				CATbb catbb =(CATbb) obj;
				
				
				switch(catbb.getIdentification())
				{
				case "1000":{
					//idenCode ="ADDI";
					regArr[Integer.parseInt(catbb.getRT(),2)]=regArr[Integer.parseInt(catbb.getRS(),2)] + Integer.parseInt(catbb.getImmediate(),2);
				}
					break;
				case "1001":
				{
					//idenCode ="ANDI";
					regArr[Integer.parseInt(catbb.getRT(),2)]=regArr[Integer.parseInt(catbb.getRS(),2)] & Integer.parseInt(catbb.getImmediate(),2);
				}
					break; 
				case "1010":
				{	//idenCode ="ORI";
					regArr[Integer.parseInt(catbb.getRT(),2)]=regArr[Integer.parseInt(catbb.getRS(),2)] | Integer.parseInt(catbb.getImmediate(),2);
					
				}
					break;
				case "1011":
				{	//idenCode ="XORI";
					regArr[Integer.parseInt(catbb.getRT(),2)]=regArr[Integer.parseInt(catbb.getRS(),2)] ^ Integer.parseInt(catbb.getImmediate(),2);
					
				}
				}
				
				
				output2.write("--------------------\n");
				output2.write("Cycle:"+Counter+"\t"+start+"\t"+catbb.getDisassembler()+"\n\n");
				output2.write("Registers");
				for(int i=0;i<32;i++)
				{
					if(i==0)
					{
						output2.write("\n"+"R00:");
					}
					else if(i==8)
					{
						output2.write("\n"+"R08:");
					}
					else if(i==16)
					{
						output2.write("\n"+"R16:");
					}
					else if(i==24)
					{
						output2.write("\n"+"R24:");
					}
					output2.write("\t"+regArr[i]);
				}
				output2.write("\n\nData");
				int di =0;
				int dix=data_index;
				while(dataMap.containsKey(dix))
				{
					if(di%8==0)
					{
						output2.write("\n"+dix+":");
					}
					output2.write("\t"+dataMap.get(dix));
					di++;
					dix+=4;
				}
				output2.write("\n\n");
			}
			else if(obj instanceof CATa)
			{
				CATa cata = (CATa) obj;
				
				output2.write("--------------------\n");
				output2.write("Cycle:"+Counter+"\t"+start+"\t"+cata.getDisassembler()+"\n\n");
				output2.write("Registers");
				
				switch(cata.getOpcode())
				{
					case "0000":
					{// J
						start =4*Integer.parseInt(cata.getInput().substring(21),2)-4;
					}
					break;
					case "0001":
					{// JR
						start = regArr[Integer.parseInt(cata.getInput().substring(6, 11),2)];
					}
					break;
					case "0010":
					{//BEQ
						if(regArr[Integer.parseInt(cata.getInput().substring(6, 11),2)]==regArr[Integer.parseInt(cata.getInput().substring(11, 16),2)])
						{
							start =start +Integer.parseInt(cata.getInput().substring(16, 32)+"00",2);
						}
					}
					break;
					case "0011":
					{// BLTZ
						if(regArr[Integer.parseInt(cata.getInput().substring(6, 11),2)]<0)
						{
							start =start +Integer.parseInt(cata.getInput().substring(16, 32)+"00",2);
						}
					}
					break;
					case "0100":
					{ // BGTZ
						if(regArr[Integer.parseInt(cata.getInput().substring(6, 11),2)]>0)
						{
							start =start +Integer.parseInt(cata.getInput().substring(16, 32)+"00",2);
						}
						
					}
					break;
					case "0101":
					{ //BREAK
						
					}
					break;
					case "0110":
					{//SW
						dataMap.put(regArr[Integer.parseInt(cata.getInput().substring(6, 11),2)]+Integer.parseInt(cata.getInput().substring(16, 32),2),regArr[Integer.parseInt(cata.getInput().substring(11, 16),2)]);
						
					}
					break;
					case "0111":
					{//LW
						//: rt ← memory[base+offset]
						regArr[Integer.parseInt(cata.getInput().substring(11, 16),2)] = dataMap.get(regArr[Integer.parseInt(cata.getInput().substring(6, 11),2)]+Integer.parseInt(cata.getInput().substring(16, 32),2));
					}
					break;
					case "1000":
					{//SLL
						// rd ← rt << sa
						String rt = cata.getInput().substring(11,16);
						String rd = cata.getInput().substring(16,21);
						String sa = cata.getInput().substring(21,26);
						String res =Integer.toBinaryString(regArr[Integer.parseInt(rt,2)]);
						String str ="";
						for(int slli=0;slli<32-res.length();slli++)
						{
							str +="0";
						}// make 32 bits
						str += res;
						str =str.substring(Integer.parseInt(sa,2));
						for(int slli =0;slli<Integer.parseInt(sa,2);slli++)
						{
							str +="0";
						}
					regArr[Integer.parseInt(rd,2)]=Complement(str);
					}
					break;
					case "1001":
					{//SRL
						String rt = cata.getInput().substring(11,16);
						String rd = cata.getInput().substring(16,21);
						String sa = cata.getInput().substring(21,26);
						
						
						String res =Integer.toBinaryString(regArr[Integer.parseInt(rt,2)]);
						String str ="";
						for(int slli=0;slli<32-res.length();slli++)
						{
							str +="0";
						}// make 32 bits
						str += res;
						str =str.substring(0,32-Integer.parseInt(sa,2));
						String temp ="";
						for(int ti=0;ti<Integer.parseInt(sa,2);ti++)
						{
							temp +="0";
						}
						str =temp +str;
						regArr[Integer.parseInt(rd,2)]=Complement(str);
					}break;
					case "1010":
					{//SRA
						String rt = cata.getInput().substring(11,16);
						String rd = cata.getInput().substring(16,21);
						String sa = cata.getInput().substring(21,26);
						
						
						String res =Integer.toBinaryString(regArr[Integer.parseInt(rt,2)]);
						String str ="";
						if(res.charAt(0)=='1')
						{
							
							for(int slli=0;slli<32-res.length();slli++)
							{
								str +="0";
							}// make 32 bits
							str += res;
							str =str.substring(0,32-Integer.parseInt(sa,2));
							String head ="";
							for(int hi=0;hi<Integer.parseInt(sa,2);hi++)
							{
								head +="1";
							}
							str =head+str;
						}
						else
						{
							for(int slli=0;slli<32-res.length();slli++)
							{
								str +="0";
							}// make 32 bits
							str += res;
							str =str.substring(0,32-Integer.parseInt(sa,2));
							String temp ="";
							for(int ti=0;ti<Integer.parseInt(sa,2);ti++)
							{
								temp +="0";
							}
							str =temp +str;
						}
						regArr[Integer.parseInt(rd,2)]=Complement(str);
						
					}break;
					case "1011":
					{
						//NOP
					}break;
				}
				
				for(int i=0;i<32;i++)
				{
					if(i==0)
					{
						output2.write("\n"+"R00:");
					}
					else if(i==8)
					{
						output2.write("\n"+"R08:");
					}
					else if(i==16)
					{
						output2.write("\n"+"R16:");
					}
					else if(i==24)
					{
						output2.write("\n"+"R24:");
					}
					output2.write("\t"+regArr[i]);
				}
				output2.write("\n\nData");
				int di =0;
				int dix=data_index;
				while(dataMap.containsKey(dix))
				{
					if(di%8==0)
					{
						output2.write("\n"+dix+":");
					}
					output2.write("\t"+dataMap.get(dix));
					di++;
					dix+=4;
				}
				
				output2.write("\n\n");
				
			}
			start +=4;
			Counter++;
		}
		output2.close();
	}
	public static int Complement(String str)
	{
		if(str.charAt(0)=='0')
		{
			return Integer.parseInt(str, 2);
		}	
		str = str.replaceAll("0", "x");
		str = str.replaceAll("1", "0");
		str = str.replaceAll("x", "1");
		
		char[] chArray =str.toCharArray();
		for(int i=str.length()-1;i>=0;i--)
		{
			if(chArray[i]=='1')
			{
				chArray[i]='0';
			}
			else
			{
				chArray[i]='1';
				break;
			}
		}
		str = String.valueOf(chArray);
		return Integer.parseInt(str,2)*-1;
	}
	
}
class CATba
{
	private final String cat_code= "11";
	private final String identification ;
	private final String rs ;
	private final String rt ;
	private final String rd ;
	private final String Term="00000000000";
	private String idenCode ="";
	
	public CATba(String input)
	{
		this.identification = input.substring(2,6);
		this.rs = input.substring(6, 11);
		this.rt = input.substring(11, 16);
		this.rd = input.substring(16,21);
		
		switch(identification)
		{
		case "0000":
			idenCode ="ADD";
			break;
		case "0001":
			idenCode ="SUB";
			break; 
		case "0010":
			idenCode ="MUL";
			break;
		case "0011":
			idenCode ="AND";
			break;
		case "0100":
			idenCode ="OR";
			break;
		case "0101":
			idenCode ="XOR";
			break;
		case "0110":
			idenCode ="NOR";
			break;
		case "0111":
			idenCode ="SLT";
		}
	}
	
	public String getIdentification()
	{
		return identification;
	}
	public String getRS()
	{
		return rs;
	}
	public String getRT()
	{
		return rt;
	}
	public String getRD()
	{
		return rd;
	}
	public String getInput()
	{
		return cat_code+identification+rs+rt+rd+Term;
	}
	public String getDisassembler()
	{
		String registerRd ="R"+(Integer.parseInt(rd,2));
		String registerRs ="R"+(Integer.parseInt(rs,2));
		String registerRt ="R"+(Integer.parseInt(rt,2));
		
		String disassemble = idenCode +" "+registerRd+", "+registerRs+", "+registerRt;
		return disassemble;
	}
	
}
class CATbb
{
	private final String cat_code= "11";
	private final String identification ;
	private final String rs ;
	private final String rt ;
	private final String immediate ;
	private String idenCode ="";
	
	public CATbb(String input)
	{
		this.identification = input.substring(2,6);
		this.rs = input.substring(6, 11);
		this.rt = input.substring(11, 16);
		this.immediate = input.substring(16,32);
		
		switch(identification)
		{
		case "1000":
			idenCode ="ADDI";
			break;
		case "1001":
			idenCode ="ANDI";
			break; 
		case "1010":
			idenCode ="ORI";
			break;
		case "1011":
			idenCode ="XORI";
		}
	}
	
	public String getIdentification()
	{
		return identification;
	}
	public String getRS()
	{
		return rs;
	}
	public String getRT()
	{
		return rt;
	}
	public String getImmediate()
	{
		return immediate;
	}
	public String getInput()
	{
		return cat_code+identification+rs+rt+immediate;
	}
	public String getDisassembler()
	{
		String imm ="#"+Integer.parseInt(immediate,2);
		String registerRs ="R"+(Integer.parseInt(rs,2));
		String registerRt ="R"+(Integer.parseInt(rt,2));
		
		String disassemble = idenCode +" "+registerRt+", "+registerRs+", "+imm;
		return disassemble;
	}
	
}
class CATa
{
	private final String cat_code= "01";
	private final String opcode ;
	private final String mips;
	private final String idenCode ;
	private String disassemble = "";
	
	public CATa(String input)
	{
		this.opcode = input.substring(2,6);
		this.mips = input.substring(6,32);
		
		switch(opcode)
		{
		case "0000":
		{
			this.idenCode ="J";
			String last = input.substring(6);
			this.disassemble =this.idenCode +" #"+(4*Integer.parseInt(last,2));
			
		}
			break;
		case "0001":
		{
			this.idenCode ="JR";
			String rs = input.substring(6,11);
			this.disassemble = this.idenCode +" R"+(Integer.parseInt(rs,2));
		}
			break; 
		case "0010":
		{
			this.idenCode ="BEQ";
			String rs = input.substring(6,11);
			String rt = input.substring(11,16);
			String offset =input.substring(16,32)+"00";
			
			this.disassemble = this.idenCode + " R"+(Integer.parseInt(rs,2))+", R"+(Integer.parseInt(rt,2))+", #"+(Integer.parseInt(offset,2));
		}
			break;
		case "0011":
		{
			this.idenCode ="BLTZ";
			String rs = input.substring(6,11);
			String offset =input.substring(16,32)+"00";
			
			this.disassemble = this.idenCode + " R"+(Integer.parseInt(rs,2))+", #"+(Integer.parseInt(offset,2));
		}
			break;
		case "0100":
		{
			this.idenCode ="BGTZ";
			String rs = input.substring(6,11);
			String offset =input.substring(16,32)+"00";
			
			this.disassemble = this.idenCode + " R"+(Integer.parseInt(rs,2))+", #"+(Integer.parseInt(offset,2));
		
		}
			break;
		case "0101":
		{
			this.idenCode ="BREAK";
			this.disassemble = this.idenCode;
		}
			break; 
		case "0110":
		{
			this.idenCode ="SW";
			String base = input.substring(6,11);
			String rt = input.substring(11,16);
			String offset = input.substring(16,32);
			
			this.disassemble = this.idenCode + " R"+(Integer.parseInt(rt,2))+", "+Integer.parseInt(offset,2)+"(R"+(Integer.parseInt(base,2))+")";
		}
			break;
		case "0111":
		{
			this.idenCode ="LW";
			String base = input.substring(6,11);
			String rt = input.substring(11,16);
			String offset = input.substring(16,32);
			
			this.disassemble = this.idenCode + " R"+(Integer.parseInt(rt,2))+", "+Integer.parseInt(offset,2)+"(R"+(Integer.parseInt(base,2))+")";
		}
			break;
		case "1000":
		{
			this.idenCode ="SLL";
			String rt = input.substring(11,16);
			String rd = input.substring(16,21);
			String sa = input.substring(21,26);
			
			this.disassemble = this.idenCode + " R"+(Integer.parseInt(rd,2))+", R"+(Integer.parseInt(rt,2))+", #"+Integer.parseInt(sa,2);
		
		}
			break; 
		case "1001":
		{
			this.idenCode ="SRL";
			String rt = input.substring(11,16);
			String rd = input.substring(16,21);
			String sa = input.substring(21,26);
			
			this.disassemble = this.idenCode + " R"+(Integer.parseInt(rt,2))+", R"+(Integer.parseInt(rd,2))+", #"+Integer.parseInt(sa,2);
		
		}
			break;
		case "1010":
		{
			this.idenCode ="SRA";
			String rt = input.substring(11,16);
			String rd = input.substring(16,21);
			String sa = input.substring(21,26);
			
			this.disassemble = this.idenCode + " R"+(Integer.parseInt(rt,2))+", R"+(Integer.parseInt(rd,2))+", #"+Integer.parseInt(sa,2);
		
		}
			break;
		case "1011":
		{
			this.idenCode ="NOP";
			this.disassemble = this.idenCode;
		}
		break;
		default:
			this.idenCode ="";
			this.disassemble="NA"+this.opcode;
		}
	}
    public String getOpcode()
	{
		return opcode;
	}
	public String getMips()
	{
		return mips;
	}
	
	public String getInput()
	{
		return cat_code+opcode+mips;
	}
	public String getDisassembler()
	{
		return disassemble;
	}
	
}
