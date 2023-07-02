package quina;

/**
 * Esta classe guarda as informacnoes do Cliente
 */
class ContaTxt {
	public static final String SEPARADOR = ",";
    private String nome; 
    private String senha;
    private String conta;
    private String saldo;
    private String credito;
    /**
     * Contrutor padrão
     */
    public ContaTxt() {      }
    /**
     * recebe a linha para popular o Objeto
     * @param line
     */
    public ContaTxt(String line) { 
    	if(null != line ){
            //Vamos quebrar a linha no separador...
            String[] dados = line.split(SEPARADOR);
            // se não for nulo, vamos setar os valores
            if(null != dados){
                setNome(dados[0]);
                setSenha(dados[1]);
                setConta(dados[2]);
                setSaldo(dados[3]);
                setCredito(dados[4]);          
                // se possuir mais campos, irá adiionar aqui, seguindo a ordem 
            }
        }   
    }
    
    public String getNome() {
        return nome;
    }
    /**
     * Temos que garantir que esta String não possua o SEPARADOR
     * Senão irá bugar
     * @param nome
     */
    public void setNome(String nome) {
        if(null != nome){
            if(nome.contains(SEPARADOR)){
                nome = nome.replaceAll(SEPARADOR, " ");
            }
        }
        this.nome = nome;
    }
    public String getSenha() {
        return senha;
    }
    public void setSenha(String senha) {
        this.senha = senha;
    }
      
    public String getConta(){
        return conta;
    }
    private void setConta(String conta) {
        this.conta = conta;
    }
    public String getSaldo(){
        return saldo;
    }
    private void setSaldo(String saldo) {
        this.saldo = saldo;
    }
     public String getCredito(){
        return credito;
    }
    private void setCredito(String credito) {
        this.credito = credito;
    }
    
    public boolean sacar(double quantia){
    	double saldo = Double.parseDouble(this.saldo);
    	double credito = Double.parseDouble(this.credito);
    	if ((saldo + credito) >= quantia) {
    		saldo -= quantia;
    		this.saldo = Double.toString(saldo);
    		return true;
    	} else
    	return false; //não tem dindin
    }
    
    public void depositar(double quantia){
        double saldo = Double.parseDouble(this.saldo);
        saldo += quantia;
        this.saldo = Double.toString(saldo);
    }
    
    /**
     * Vamos concatenar os dados para salvar..
     */
    public String gerarString(){
        final StringBuffer buffer = new StringBuffer();
        if(null != getNome()){
            buffer.append(getNome());
        }
        //  inserimos ao separador
        buffer.append(SEPARADOR);
        if(null != getSenha()){
            buffer.append(getSenha());
        }
        
        buffer.append(SEPARADOR);
        if(null != getConta()){
            buffer.append(getConta());
        }
        
        buffer.append(SEPARADOR);  
        buffer.append(getSaldo());
        
        buffer.append(SEPARADOR);
        buffer.append(getCredito());
        
        return buffer.toString();
    }
}
