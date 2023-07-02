package quina;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Random;
import java.awt.Font;

import static javax.swing.BorderFactory.createEtchedBorder;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Atm extends JDialog {
	private JPanel jPanel1, jPanel2;
    private JScrollPane jScrollPane1;
    private JLabel lbTitulo, lbNome, lbSenha, lbConta, lbValor, lbInfo;
    private JButton btCadastrar, btConsultar, btExcluir, btAcessar, btTransferir, btJogar, btSair;
    private JTextField tfNome, tfSenha,tfConta, tfValor;
    private JTextArea taSaida;
	
 // VARIAVEIS
    DecimalFormat df = new DecimalFormat("##,#00.00");
    List<ContaTxt> todasContasLidas;//<-"cadastro.txt"
    static ContaTxt contatxtLidaAtual;
    static String nomex="i";
     static String contax="";
    static String senhax="";
    static String saldox="";
    static String creditox="";
    static String nomeLog="";
    static String contaLog="";
    static String senhaLog="";
    static String saldoLog="";
    static boolean secure = false;
    boolean primaConta = false;
    Random bonus = new Random();
    JComboBox combobox = new JComboBox();
    
	
	public Atm(Frame frame) {
        super(frame,true);
        initComponents();            
     }
	
	private void initComponents() {
		Font fonte1 = new Font("Trebuchet MS", 1, 38);
	    Font fonte2 = new Font("Arial", 1, 12);       
	    lbTitulo = new JLabel("X-BANK");
	    lbTitulo.setFont(fonte1);        
	    jPanel1 = new JPanel();
	    lbNome = new JLabel("Nome:");
	    lbNome.setFont(fonte2);    
	    lbSenha = new JLabel("Senha:");
	    lbSenha.setFont(fonte2);
	    lbConta = new JLabel("Conta:");
	    lbConta.setFont(fonte2);
	    tfNome = new JTextField();
	    jPanel1.setBorder(createEtchedBorder());
	    tfSenha = new JTextField();
	    tfConta = new JTextField();
	    tfValor = new JTextField();  
	    jScrollPane1 = new JScrollPane();   
	    btCadastrar = new JButton("Abrir outra conta");
	    if(!"".equals(nomeLog))btCadastrar.setEnabled(false);
	    btConsultar = new JButton("Pesquisar");
	    btExcluir = new JButton("Excluir minha conta");
	    if("".equals(nomeLog))btExcluir.setEnabled(false);
	    btAcessar = new JButton("Acessar minha conta");
	    if(!"".equals(nomeLog))btAcessar.setEnabled(false);
	    btTransferir = new JButton("Transferir");
	    if("".equals(nomeLog))btTransferir.setEnabled(false);
	    btJogar = new JButton("Voltar ao jogo");
	    
	    btSair = new JButton("Sair da conta atual");
	    if("".equals(nomeLog))btSair.setEnabled(false);
	    jPanel2 = new JPanel();
	    jPanel2.setBorder(createEtchedBorder());
	    lbValor = new JLabel("Valor:");
	    lbValor.setFont(fonte2);
	 // ler cadastro.txt (se tiver, senão é a conta 1)    
	    try{
	    todasContasLidas = Cadastro.leCntas();
	        for(final ContaTxt c :  todasContasLidas){
	            String contasCad = c.getConta()+" "+c.getNome();
	            //No caso de haver um arquivo vazio
	            if(c.getNome()!= null) combobox.addItem(contasCad); else {
	                primaConta = true;
	                btAcessar.setEnabled(false);
	       }
	        }
	    } catch (IOException ex) {
	        ex.printStackTrace();
	        //se caiu aqui,cadastro.txt não existe
	        primaConta = true;
	        btAcessar.setEnabled(false);
	    } 
// colocar na combobox    
	    combobox.setEditable(false);
	    combobox.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent e) {
	          if(!primaConta){
	           int pesquisada=combobox.getSelectedIndex();
	    ContaTxt c= todasContasLidas.get(pesquisada);//retorna a contatxtLidaAtual
	    tfNome.setText(c.getNome());
	    tfSenha.setText("");
	    tfConta.setText(c.getConta());
	          }
	        }
	    });
	 // colocar na area de texto   
	    taSaida = new JTextArea();
	    taSaida.setBorder(createEtchedBorder());
	    taSaida.setEditable(false);
	    jScrollPane1.setViewportView(taSaida);  
	    lbInfo = new JLabel();	    
	  //colocar no label (se houver conta)
	    if(!primaConta){
	    lbInfo.setText("Contas cadastradas: "+ ( todasContasLidas.size()));
	    }
	 
	// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 
	// BOTAO CADASTRAR
	    btCadastrar.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent arg0) {
	            try {            
	                nomex = tfNome.getText();
	                senhax = tfSenha.getText();
	                if ((nomex.matches("[a-z]{2,}")) &&(senhax.matches("[a-z]{2,2}"))){           
	                    int numCnta=0;
	                    int aux;
//	      Determinar o numero da conta a partir da ultima cadastrada
	                  if(!primaConta){   
	                    try{   
	                         todasContasLidas = Cadastro.leCntas();
	                        for(final ContaTxt c :  todasContasLidas){
	                            aux=Integer.parseInt(c.getConta());
	                            if (aux>numCnta) numCnta = aux;
	                        }
	                    } catch (IOException e) {
	                        e.printStackTrace();
	                    }  
	                }
	                    contax = Integer.toString(numCnta+1);
	                    creditox = Double.toString(bonus.nextInt(30)*5000 + 50000);
//	      Gerar a string de dados da conta
	                    String dadosConta = nomex+","+senhax+","+contax+",0.0,"+creditox;
	                    String contaCad = contax +" "+nomex;
//	      Gerar o objeto 'contatxt' do tipo(classe) ContaTxt            
	                    contatxtLidaAtual=new ContaTxt(dadosConta);                   
//	      Atualizando com o saldo gerado (se nao for a conta 1)
	                if(!primaConta)
	                    //contasLidas = Cadastro.leCntas();
	                       // for(final ContaTxt c : contasLidas)
	                    todasContasLidas.add(contatxtLidaAtual);//nem precisa pois sempre le o txt
	                    //e tambem dava excessao na 1a. conta
	                    //System.out.println("gravando...");
//	      Salvar no arquivo cadastro.txt
	                    Cadastro.gravaCliente(contatxtLidaAtual);
//	      Atualizar a tela             
	                    combobox.addItem(contaCad);  
	                    taSaida.setText("Conta "+ contax + " cadastrada. Bem vindo, "+ nomex+"!\nVocê é nosso cliente especial\n"
	                            + "e por isso o \"X-BANK\" está\n"
	                            + "lhe oferecendo um crédito de "+df.format(Double.parseDouble(creditox))+". \nDesejamos a você muita sorte!");
	                    if (primaConta)
	                        lbInfo.setText("Contas Cadastradas: 1" );
	                    else
	                        lbInfo.setText("Contas Cadastradas: " + (todasContasLidas.size()));
//	      Autenticar (Logar)
	                    nomeLog = nomex;
	                    contaLog = contax;
	                    senhaLog = senhax;
	                    saldoLog=saldox;
	                    tfConta.setText(contax);
	                    secure = true;
	                    JOptionPane.showMessageDialog(rootPane, "Conta cadastrada com sucesso!");
	                    btAcessar.setEnabled(true);
	                    btCadastrar.setEnabled(false);
	                    btAcessar.doClick();
	                } // FIM if nome/senha.matches
	                else
	                    JOptionPane.showMessageDialog(rootPane, "Nome:  Pelo menos 3 letras (minúsculas, sem espaço)\nSenha: (Apenas 2 minúsculas)", "Aviso", 1);
	            }catch (Exception e) {
	                JOptionPane.showMessageDialog(rootPane, "Bem vindo!");
	               // btAcessar.doClick();//caso o usuario esqueca
	            } //FIM de Try/catch
	        }   // FIM de Event
	    });//FIM de btCadastrar
	// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 
	// BOTAO CONSULTAR
	    btConsultar.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent arg0) {
	            try {     
	                boolean naoTem = true;
	                nomex = tfNome.getText();
	                contax = tfConta.getText();
	                 todasContasLidas = Cadastro.leCntas();
	                String resultadoConsulta = "";
	                for(final ContaTxt c :  todasContasLidas){
	                    if((c.getNome().equals(nomex))||(c.getConta().equals(contax))){
	                        resultadoConsulta +=("Nome: " + c.getNome() +  "\tConta: " + c.getConta() + "\n");
	                        if((c.getNome().equals(nomex))&&(c.getConta().equals(contax))){
	                        tfNome.setText(c.getNome());
	                        tfConta.setText(c.getConta());
	                        }
	                        naoTem=false;
	                    }
	                }               
	                if (naoTem)
	                    taSaida.setText("Nenhum resultado para esta consulta");
	                else
	                    taSaida.setText(resultadoConsulta);
	                
	                tfSenha.setText("");
	                
	            } catch (Exception E) {
	                JOptionPane.showMessageDialog(rootPane, "Nenhum resultado para esta consulta");
	            }
	        }// FIM de Event
	    });//FIM de btConsultar
	// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 
	// BOTAO EXCLUIR
	    btExcluir.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent arg0) {
	            if (!"".equals(nomeLog)){
	                //nome = tfNome.getText();
	                int posicao=0;
	                String contaAExcluir="";
	                try {
	                     todasContasLidas = Cadastro.leCntas();
	                } catch (IOException e) {
	                        e.printStackTrace();
	                }
	                for(final ContaTxt c :  todasContasLidas){
	                    if((c.getConta().equals(contaLog))){
	                        posicao =  todasContasLidas.indexOf(c);//indice a ser excluido na memoria
	                        contaAExcluir=c.getNome()+","+c.getSenha()
	                              +","+c.getConta()+","+c.getSaldo()
	                             +","+c.getCredito();    
	                    }
	                }
	                if (senhaLog.equals(JOptionPane.showInputDialog(rootPane,"Digite a senha\n para excluir a conta: "+nomeLog))){
	                    secure=false; 
	                    combobox.removeItemAt(posicao);
	                     todasContasLidas.remove(posicao);//exclui na memoriaTESTAR DEPOIS PRA VER TAMANHO Q MOSTRA
	                    Cadastro.excluirLinha(contaAExcluir);//exclui no arquivo
	                    tfNome.setText("");
	                    tfSenha.setText("");
	                    tfConta.setText("");
	                    taSaida.setText("");
	                    nomeLog = "";
	                    contaLog = "";
	                    senhaLog = ""; 
	                    
	                    if (primaConta){
	                        //btAcessar.setEnabled(false);
	                        lbInfo.setText("Nenhuma contas cadastrada");
	                    }else
	                    lbInfo.setText("Contas Cadastradas: " + ( todasContasLidas.size()));
	                    JOptionPane.showMessageDialog(rootPane, "Conta excluida com sucesso!", "Confirmado",
	                    JOptionPane.INFORMATION_MESSAGE);
	                    btSair.doClick();
	                }
	                else  
	                    JOptionPane.showMessageDialog(rootPane, "Operação cancelada", "Cancelado", JOptionPane.INFORMATION_MESSAGE);                 
	            } else
	                JOptionPane.showMessageDialog(rootPane, "Você deve estar logado\n na conta para excluí-la.");
	        }
	    });//FIM de btExcluir
	// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 
	// BOTAO ACESSAR
	    btAcessar.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent arg0){
	            double total=0,saldo=0,credito=0;
	            
	            if (secure == true){
	                nomex = nomeLog;
	                senhax = senhaLog;
	                contax = contaLog;
	            }
	            else{
	                nomex = tfNome.getText();
	                senhax = tfSenha.getText();
	                contax = tfConta.getText();
	            }
	            String dadosConta="";
	            try {
	                 todasContasLidas = Cadastro.leCntas();
	            } catch (IOException e) {
	                        e.printStackTrace();
	            }

	            for(final ContaTxt c :  todasContasLidas){
	                if((c.getNome().equals(nomex))&&(c.getSenha().equals(senhax))&&(c.getConta().equals(contax))){
	                    dadosConta=c.getNome()+","+c.getSenha()+","+
	                            c.getConta()+","+c.getSaldo()+","+
	                            c.getCredito();
	                    nomex=c.getNome();
	                     contax=c.getConta();
	                   saldo=Double.parseDouble(c.getSaldo());
	                   credito =Double.parseDouble(c.getCredito());
	                   total=credito+saldo;
	                    break;
	                }
	            }
	            if ("".equals(dadosConta))
	                JOptionPane.showMessageDialog(rootPane, "Nao confere.\n Preecha os 3 campos \ne tente novamente");
	            else{
	                contatxtLidaAtual = new ContaTxt(dadosConta);
	                taSaida.setText("Nome: " + nomex + "\nConta: " + contax +"\nSaldo: " + df.format(saldo)+"\nCredito: "+ df.format(credito)+"\nTotal: "+df.format(total));
	                tfNome.setText(nomex);
	                tfSenha.setText("");
	                tfConta.setText(contax);
	                lbInfo.setText("Você está acessando a conta:  " + contatxtLidaAtual.getConta() +" - "+ contatxtLidaAtual.getNome());
	                nomeLog=contatxtLidaAtual.getNome();
	                contaLog=contatxtLidaAtual.getConta();
	                senhaLog=contatxtLidaAtual.getSenha();
	                saldoLog=contatxtLidaAtual.getSaldo();
	                secure=true;
	                btAcessar.setEnabled(false);
	                btSair.setEnabled(true);
	                 btTransferir.setEnabled(true);
	                  btCadastrar.setEnabled(false);
	                  btExcluir.setEnabled(true);
	            
	            }
	        }//Fim de Event
	    });//FIM deAcessar
	   if (secure){
	       btAcessar.setEnabled(true);
	       btAcessar.doClick();}
	 // DEBUG //  System.out.println("secure = "+secure);
	// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 
	// BOTAO TRANSFERIR
	    btTransferir.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent arg0) {
	            //secure == true significa que está logado
	            if (secure){
	                System.out.print("Da contaLog: "+contaLog);
	                nomex = tfNome.getText();
	                contax = tfConta.getText();
	                System.out.println(" para conta destino: "+contax);
	                //if((contax != null) && !contaLog.equals(contax)){
	                //if (contaLog == null ? contax == null : !contaLog.equals(contax)) {
	                if(contax == null || contaLog.equals(contax))JOptionPane.showMessageDialog(rootPane, "A conta de origem e a conta de destino sao as mesmas!"); else {
	                    try {
	                        double total = 0,sald=0,credit=0;
	                        boolean contaCerta = false;
	                        boolean naoDepositou=true;
	                        boolean sacou=false;
	                        double valor = Double.parseDouble(tfValor.getText());
	                        //1o. populamos o objeto da classe List<ContaTxt>
	                        todasContasLidas = Cadastro.leCntas();
	                        String linhaAlterada="";                       
	                        //vamos ver se a conta destino esta correta
	                        for(final ContaTxt d : todasContasLidas){     
	                            if((d.getNome().equals(nomex)) && (d.getConta().equals(contax)))
	                                contaCerta=true;                                                                   
	                        }
	                        //depois localizamos a conta logada
	                        if(contaCerta){                           
	                            for(final ContaTxt c : todasContasLidas){
	                                if((c.getNome().equals(nomeLog)) && (c.getConta().equals(contaLog))){
	                                    
	                                    //Copiando os dados antes de alterar
	                                    String dadosConta=c.getNome()+","+c.getSenha()
	                                            +","+c.getConta()+","+c.getSaldo()
	                                            +","+c.getCredito();
	                                    
	                                    System.out.println("Conta: "+c.getConta()+" - > Saldo: " +c.getSaldo()+
	                                            " + Credito: " +c.getCredito());
	                                    //SACANDO (se tiver credito)
	                                    if (!(c.sacar(valor))){
	                                        System.out.println(c.getSaldo()+" < -- saldo negativo !!! ");
	                                        JOptionPane.showMessageDialog(rootPane, "Saldo Insuficiente!");
	                                    }
	                                    else {
	                                        System.out.println("Conta: "+c.getConta()+" Saque: "+valor+" - > Saldo: " +c.getSaldo()+
	                                                " + Credito: " +c.getCredito()+"<- sacou? ");
	                                        sald=Double.parseDouble(c.getSaldo());
	                                        credit =Double.parseDouble(c.getCredito());
	                                        total=credit+sald;
	                                        taSaida.setText("Nome: " + c.getNome() +  "\nConta: " + c.getConta() +
	                                                "\nSaldo: " + df.format(sald)+"\nCredito: "+
	                                                df.format(credit)+"\nTotal: "+df.format(total));
	                                        //Obtendo a linha alterada para gravar
	                                        linhaAlterada=c.getNome()+","+c.getSenha()
	                                                +","+c.getConta()+","+c.getSaldo()
	                                                +","+c.getCredito();
	                                        Cadastro.replaceLinha(dadosConta, linhaAlterada) ;
	                                        todasContasLidas = Cadastro.leCntas();//Se nao atualizar dá pra jogar mesmo com a conta zerada
	                                        contatxtLidaAtual = new ContaTxt(linhaAlterada);//Essa linha que atualiza na memoria; 
	                                        sacou=true;
	                                    }
	                                    break;
	                                }
	                            }
	                            
	                            System.out.println(" - - - - - - 'Deposito' - - - - - -");
	                            //Depositando
	                            if(sacou){
	                                for(final ContaTxt c : todasContasLidas){
	                                    if((c.getNome().equals(nomex)) && (c.getConta().equals(contax))){
	                                        System.out.println("Na conta: "+c.getConta()+" - > Saldo: " +c.getSaldo()+
	                                                " & Credito: " +c.getCredito());
	                                        //Copiando a linha antes de alterar
	                                        String dadosConta=c.getNome()+","+c.getSenha()
	                                                +","+c.getConta()+","+c.getSaldo()
	                                                +","+c.getCredito();
	                                        c.depositar(valor);
	                                        System.out.println("Na conta: "+c.getConta()+" - > Saldo: " +c.getSaldo()+
	                                                " após deposito de : " +valor);
	                                        //Obtendo a linha alterada
	                                        linhaAlterada=c.getNome()+","+c.getSenha()
	                                                +","+c.getConta()+","+c.getSaldo()
	                                                +","+c.getCredito();
	                                        Cadastro.replaceLinha(dadosConta, linhaAlterada) ;
	                                        //Ta salvo! Dando a boa noticia...
	                                        JOptionPane.showMessageDialog(rootPane, "Valor Transferido com sucesso.");
	                                        TelaJogo.transferiu = true;
	                                        naoDepositou=false;
	                                        break;
	                                    }
	                                }
	                                if (naoDepositou)
	                                    JOptionPane.showMessageDialog(rootPane, "Ocorreu um erro... \nNão depositou...");
	                            }
	                        }
	                        else
	                            JOptionPane.showMessageDialog(rootPane, "Preencha corretamente\n"
	                                    + "     o nome e o numero da conta");
	                    } catch (Exception e) {
	                        JOptionPane.showMessageDialog(rootPane, "Ocorreu um erro... \nPreecha os campos:\nNome, Conta, Valor (Ex.: 12500.75) \ne tente novamente");
	                    }
	                }
	            }
	            else
	                JOptionPane.showMessageDialog(rootPane, "Você não está logado!");

	        }
	    });//FIM de btTransferir
	// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 
	// BOTAO JOGAR
	    btJogar.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent arg0) {
	            dispose();
	        }// FIM de Event
	    });//FIM de btJogar
	// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 
	// BOTAO SAIR
	    btSair.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent arg0) {
	            tfNome.setText("");
	                    tfSenha.setText("");
	                    tfConta.setText("");
	                    taSaida.setText("");
	                    lbInfo.setText("Consulte pelo nome ou só o número");
	                    nomeLog="";
	                    contaLog="";
	                    senhaLog="";
	                    secure=false;
	                    if (primaConta)
	                        btAcessar.setEnabled(false);
	                    else
	                    btAcessar.setEnabled(true);
	                    btCadastrar.setEnabled(true);
	                    btSair.setEnabled(false);
	                    btExcluir.setEnabled(false);
	        }// FIM de Event
	    });//FIM de btSair
	//Posicionando os componentes na Tela
	// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 
	    //objeto.setBounds(posicaoColuna,posicaoLinha,comprimentodalinha,alturadalinha);
	    lbTitulo.setBounds(30, 18, 390, 45); 
	    jPanel1.setBounds(10, 80, 420, 340);
	    jPanel2.setBounds(180, 90, 240, 66);
	    lbNome.setBounds(20,90,50,20);
	    tfNome.setBounds(80,90,80,24);
	    lbConta.setBounds(20,132,50,20);
	    tfConta.setBounds(80,132,80,24);
	    lbSenha.setBounds(20,174,50,20);
	    tfSenha.setBounds(80,174,80,24);
	    lbValor.setBounds(210,110,40,20);
	    lbInfo.setBounds(70, 425, 308, 20);
	    tfValor.setBounds(260,110,120,24);
	      
	    btAcessar.setBounds(20, 215, 200, 24);
	    btSair.setBounds(228, 215, 192, 24);
	    btJogar.setBounds(20, 386, 150, 24);

	    btCadastrar.setBounds(20, 260, 200, 24);
	    btExcluir.setBounds(228, 260, 192, 24);
	    btConsultar.setBounds(20, 330, 150, 24);
	    combobox.setBounds(20, 300, 150, 24);
	    btTransferir.setBounds(260, 164, 120, 24); 
	    jScrollPane1.setBounds(180, 300, 240, 110);
	    
	    getContentPane().add(lbTitulo);       
	    getContentPane().add(btCadastrar);
	    getContentPane().add(combobox);
	    getContentPane().add(btConsultar);
	    getContentPane().add(btAcessar);
	    getContentPane().add(btExcluir);
	    getContentPane().add(btTransferir);
	    getContentPane().add(btJogar);
	    getContentPane().add(btSair);
	    getContentPane().add(tfNome);
	    getContentPane().add(tfSenha);
	    getContentPane().add(tfConta);
	    getContentPane().add(tfValor);    
	    getContentPane().add(lbNome);
	    getContentPane().add(lbSenha);
	    getContentPane().add(lbConta);
	    getContentPane().add(lbValor);
	    getContentPane().add(lbInfo);
	    getContentPane().add(jPanel2);
	    getContentPane().add(jScrollPane1);
	    getContentPane().add(jPanel1); 
	    
	    setSize(448, 502);
	    //setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	    setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	    getContentPane().setLayout(null);
	    setResizable(false);
	    setVisible(true);	    
	    
	    
	}//FIM de initComponents
}
