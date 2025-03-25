package quina;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Cadastro {

	public static final String ARQUIVO = "cadastro.txt";
	
    public static void gravaCliente(ContaTxt clienteToTxt) throws IOException{
    	File arquivo = new File(ARQUIVO);
    	try {
    		if (!arquivo.exists()) {
    		arquivo.createNewFile();
    		}
    	FileWriter fw = new FileWriter(ARQUIVO,true);
    	PrintWriter pw = new PrintWriter(fw);
    	pw.println(clienteToTxt.gerarString());
    	fw.flush();
    	fw.close();
    	} catch (IOException ex) {
    		ex.printStackTrace();
    		}
    }

    public static List<ContaTxt> leCntas() throws IOException{
    	File arquivo = new File(ARQUIVO);
        List<ContaTxt> listOnTxt = new ArrayList<ContaTxt>(0);
    	try {
    		if (arquivo.exists()) {
    	
        FileReader arq = new FileReader(ARQUIVO);
        BufferedReader lerArq = new BufferedReader(arq);
        String linha = lerArq.readLine();
        ContaTxt clientela = new ContaTxt(linha);
        listOnTxt.add(clientela);        

        while (linha != null){
            linha = lerArq.readLine();
            if(null != linha && !"".equals(linha) ){
                clientela = new ContaTxt(linha);
                listOnTxt.add(clientela);
            }
        }
        arq.close();
       
    		}
    	} catch (IOException ex) {
    		System.out.println("Abra uma conta.");
    		}
    	 return listOnTxt;
    }

    public static void replaceLinha( String linhaAlterar, String linhaAlterada) {
    File f = new File(ARQUIVO);    
    File nf = new File("temporario.tmp");
        FileWriter fw = null;
        Scanner s = null;
        try {
            fw = new FileWriter(nf);
            s = new Scanner(f);

            while (s.hasNextLine()) {
                String linha = s.nextLine();

                linha = linha.replace(linhaAlterar, linhaAlterada);

                try {
                    fw.write(linha + System.getProperty("line.separator"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fw.close();
                s.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        f.delete();
        nf.renameTo(f);
    }

    public static void excluirLinha(String contaAExcluir) {                                                  
        FileReader fileReader = null;  
        FileWriter fileWriter = null;   
        BufferedReader leitor = null;  
        String nomeDoArquivo = ARQUIVO;
        File orig = new File(nomeDoArquivo);
        String arquivoConferir = "conferir2.txt";
        File novo = new File(arquivoConferir);
        String line = "";  
            try {  
                fileReader = new FileReader(new File(nomeDoArquivo));  
                fileWriter = new FileWriter(new File(arquivoConferir)); 
                leitor = new BufferedReader(fileReader);
                line = "";  
                while ((line = leitor.readLine()) != null) {  
                    if(!line.trim().equals(contaAExcluir.trim())) { 
                            fileWriter.write(line + "\r\n");      
                    }  
                }  
            } catch (IOException e) {  
                e.printStackTrace();  
            } finally   {  
                try {  
                    fileWriter.close();
                    fileReader.close();
                    orig.delete();
                    novo.renameTo(orig);
                } catch (IOException e) {  
                    e.printStackTrace();  
                } 
            } 
    }
}
