package pacoteBase.VIEW;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;

import javax.swing.*;
import pacoteBase.CONTROL.ControlarAplicativo;

public class MontarPainelInicial  {

	private JFrame   baseFrame;
	private JPanel   basePanel;
	private JPanel   outputPanel, outputPanelEsq, outputPanelCen, outputPanelDir;
	private JPanel   controlePanelAcao1;
	private JPanel   controlePanelAcao2;
	private JPanel   controlePanelAcao3;
	private JPanel   controlePanelAcao5;
	private JPanel   controlePanelAcao4;

	private JButton  btAcao3;
	private JButton  btAcao1;
	private JButton  btSalva;
	private JButton  btReset;
	private JButton  btAcao4;
	private JButton  btSalvarLzw;
	private JButton  btAbrirLzw;

	private JRadioButton	btAcao31; 
	private JRadioButton 	btAcao32;
	private JRadioButton 	btAcao33;
	private JTextField 		txt31;
	private ButtonGroup  	btRdAcao3;

	private JTextField txt11;
	private JTextField txt12;
	private JTextField txt13;

	private JTextField txt21;
	private JTextField txt22;

	private JRadioButton  btAcao51;
	private JRadioButton  btAcao52;
	private JRadioButton  btAcao53;
	private ButtonGroup   btRdVisualImg;

	private JLabel lbAcao41;
	private JLabel lbAcao42;
	private JLabel lbAcao43;
	private JLabel lbAcao44;

	private Graphics      desenhoCen;
	private Graphics      desenhoDir;

	//*******************************************************************************************
	public MontarPainelInicial( ControlarAplicativo controlePrograma )
	{
		JPanel  buttonPanel;
		JPanel  titlePanel;
		JPanel  acao3Panel;
		JPanel  acao1Panel;
		JPanel  acao2Panel;
		JPanel  acao5Panel;
		JPanel  acao4Panel;

		// LAYOUT
		baseFrame = new JFrame();
		baseFrame.setLayout( new BoxLayout( baseFrame.getContentPane(), BoxLayout.Y_AXIS) );

		baseFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);  // FITS PANEL TO THE ACTUAL MONITOR SIZE
		baseFrame.setUndecorated(true);  // TURN OFF ALL THE PANEL BORDERS 

		basePanel = new JPanel();
		basePanel.setLayout( new BorderLayout() );

		// TITLE PANEL
		titlePanel = new JPanel();
		titlePanel.setPreferredSize( new Dimension ( 0, 50 ) );
		titlePanel.setBackground( Color.gray );

		// OUTPUT PANEL
		outputPanel = new JPanel();
		outputPanel.setLayout( new BorderLayout() );

		outputPanelEsq = new JPanel();
		outputPanelEsq.setPreferredSize( new Dimension ( 130, 0 ) );
		outputPanelEsq.setLayout( new BoxLayout (outputPanelEsq, BoxLayout.Y_AXIS));
		outputPanelEsq.setBackground( Color.lightGray );

		outputPanelCen = new JPanel();
		outputPanelCen.setBackground( new Color ( 220, 220, 210 ) );
		outputPanelCen.setLayout( new BorderLayout() );

		outputPanelDir = new JPanel();
		outputPanelDir.setBackground( new Color ( 210, 200, 200 ) );
		outputPanelDir.setPreferredSize( new Dimension ( 580, 0 ) );
		outputPanelDir.setLayout( new BorderLayout() );

		// BUTTON PANEL
		buttonPanel = new JPanel();
		buttonPanel.setPreferredSize( new Dimension ( 0, 50 ) );
		buttonPanel.setBackground( Color.gray );

		// PANEL TITLE
		JLabel titulo;
		titulo = new JLabel( "Mediana, Canny e LZW");
		titulo.setForeground(Color.black);
		titulo.setFont(new Font("Dialog", Font.BOLD, 25));
		titlePanel.add(titulo);

		// ADDING BUTTONS
		addAButton ( "New Image", "botaoImagem", buttonPanel, true, Color.white, controlePrograma );
		btAbrirLzw = addAButton ( "Abrir LZW", "abrirlzw", buttonPanel, false, Color.white, controlePrograma );
		btReset = addAButton ( "Reset", "botaoReset", buttonPanel, false, new Color(50, 150, 255), controlePrograma );
		btAcao1 = addAButton ( "Destacar", "botaoAcao1", buttonPanel, false, new Color(255, 255, 50), controlePrograma );
		btAcao3 = addAButton ( "Salientar", "botaoAcao3", buttonPanel, false, new Color(255, 255, 50), controlePrograma );
		btAcao4 = addAButton ( "Ruído Sal e Pimenta", "botaoAcao4", buttonPanel, false, new Color(255, 255, 50), controlePrograma );
		btSalva = addAButton ( "Salvar", "botaoSalva", buttonPanel, false, new Color(50, 255, 150), controlePrograma );
		btSalvarLzw = addAButton ( "Salvar LZW", "salvarlzw", buttonPanel, false, new Color(50, 255, 150), controlePrograma );
		addAButton ( "END", "botaoFim", buttonPanel, true, new Color(255, 50, 50), controlePrograma );

		// ADDING RADIO BUTTON PARA CONTROLE DA ACAO3
		controlePanelAcao3 = new JPanel();
		controlePanelAcao3.setBackground( Color.lightGray );
		controlePanelAcao3.setMaximumSize( new Dimension ( 130, 120 ) );
		outputPanelEsq.add( controlePanelAcao3 );

		btAcao31 = new JRadioButton ( "3x3", true );
		btAcao32 = new JRadioButton ( "5x5", false );
		btAcao33 = new JRadioButton ( "7x7", false );

		btRdAcao3 = new ButtonGroup();
		btRdAcao3.add(btAcao31);
		btRdAcao3.add(btAcao32);
		btRdAcao3.add(btAcao33);

		btAcao31.addActionListener(controlePrograma);
		btAcao32.addActionListener(controlePrograma);
		btAcao33.addActionListener(controlePrograma);

		btAcao31.setActionCommand("Acao31");
		btAcao32.setActionCommand("Acao32");
		btAcao33.setActionCommand("Acao33");

		acao3Panel = new JPanel();
		acao3Panel.setPreferredSize( new Dimension ( 120, 115 ) );
		acao3Panel.setLayout(new GridLayout(5, 1));

		acao3Panel.add( btAcao31 );
		acao3Panel.add( btAcao32 );
		acao3Panel.add( btAcao33 );

		txt31 = addTextField("Tamanho", "filtro", acao3Panel, controlePrograma);

		acao3Panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Filtro Mediana"));

		controlePanelAcao3.add(acao3Panel);
		controlePanelAcao3.setVisible(false);

		// ADDING RADIO BUTTON PARA CONTROLE DA ACAO 1
		controlePanelAcao1 = new JPanel();
		controlePanelAcao1.setBackground( Color.lightGray );
		controlePanelAcao1.setMaximumSize( new Dimension ( 130, 80 ) );
		outputPanelEsq.add( controlePanelAcao1 );

		acao1Panel = new JPanel();
		acao1Panel.setPreferredSize( new Dimension ( 120, 75 ) );
		acao1Panel.setLayout(new GridLayout(3, 1));

		txt11 = addTextField("Padrão", "padrao", acao1Panel, controlePrograma);
		txt12 = addTextField("Superior", "superior", acao1Panel, controlePrograma);
		txt13 = addTextField("Inferior", "inferior", acao1Panel, controlePrograma);

		acao1Panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Filtro Canny"));

		controlePanelAcao1.add(acao1Panel);
		controlePanelAcao1.setVisible(false);

		// ADDING RADIO BUTTON PARA CONTROLE DA ACAO 2
		controlePanelAcao2 = new JPanel();
		controlePanelAcao2.setBackground( Color.lightGray );
		controlePanelAcao2.setMaximumSize( new Dimension ( 130, 65 ) );
		outputPanelEsq.add( controlePanelAcao2 );

		acao2Panel = new JPanel();
		acao2Panel.setPreferredSize( new Dimension ( 120, 60 ) );
		acao2Panel.setLayout(new GridLayout(2, 1));

		txt21 = addTextField("Qtd. Sal", "sal", acao2Panel, controlePrograma);
		txt22 = addTextField("Qtd. Pim.", "pim", acao2Panel, controlePrograma);

		acao2Panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Ruído Sal e Pim."));

		controlePanelAcao2.add(acao2Panel);
		controlePanelAcao2.setVisible(false);
		
		// ADDING RADIO BUTTON PARA CONTROLE DO TIPO DA ACAO 5
		controlePanelAcao5 = new JPanel();
		controlePanelAcao5.setBackground( Color.lightGray );
		controlePanelAcao5.setMaximumSize( new Dimension ( 130, 80 ) );
		outputPanelEsq.add( controlePanelAcao5 );

		btAcao51 = new JRadioButton ( "Vermelho", true );
		btAcao52 = new JRadioButton ( "Verde", false );
		btAcao53 = new JRadioButton ( "Azul", false );

		btRdVisualImg = new ButtonGroup();
		btRdVisualImg.add(btAcao51);
		btRdVisualImg.add(btAcao52);
		btRdVisualImg.add(btAcao53);

		btAcao51.addActionListener(controlePrograma);
		btAcao52.addActionListener(controlePrograma);
		btAcao53.addActionListener(controlePrograma);
		
		btAcao51.setActionCommand("Acao51");
		btAcao52.setActionCommand("Acao52");
		btAcao53.setActionCommand("Acao53");

		acao5Panel = new JPanel();
		acao5Panel.setPreferredSize( new Dimension ( 120, 70 ) );
		acao5Panel.setLayout(new GridLayout(3, 1));

		acao5Panel.add( btAcao51 );
		acao5Panel.add( btAcao52 );
		acao5Panel.add( btAcao53 );

		acao5Panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Cor da borda"));

		controlePanelAcao5.add(acao5Panel);
		controlePanelAcao5.setVisible(false);

		outputPanelDir.addMouseListener(controlePrograma);
		outputPanelDir.addMouseListener(controlePrograma);

		// ADDING RADIO BUTTON PARA CONTROLE DO TIPO DA ACAO 4
		controlePanelAcao4 = new JPanel();
		controlePanelAcao4.setBackground( Color.lightGray );
		controlePanelAcao4.setMaximumSize( new Dimension ( 130, 100 ) );
		outputPanelEsq.add( controlePanelAcao4 );

		acao4Panel = new JPanel();
		acao4Panel.setPreferredSize( new Dimension ( 120, 90 ) );
		acao4Panel.setLayout(new GridLayout(4, 1));
		
		lbAcao41 = new JLabel();
		lbAcao42 = new JLabel();
		lbAcao43 = new JLabel();
		lbAcao44 = new JLabel();

		acao4Panel.add( lbAcao41 );
		acao4Panel.add( lbAcao42 );
		acao4Panel.add( lbAcao43 );
		acao4Panel.add( lbAcao44 );

		acao4Panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Cor"));

		controlePanelAcao4.add(acao4Panel);
		controlePanelAcao4.setVisible(false);

		// VISIBLE PANELS
		outputPanel.add( outputPanelEsq, BorderLayout.LINE_START );
		outputPanel.add( outputPanelCen, BorderLayout.CENTER );
		outputPanel.add( outputPanelDir, BorderLayout.LINE_END );

		basePanel.add( titlePanel, BorderLayout.PAGE_START );
		basePanel.add( outputPanel, BorderLayout.CENTER );
		basePanel.add( buttonPanel, BorderLayout.PAGE_END );

		baseFrame.add(basePanel);
		baseFrame.setVisible(true);
	}

	//*******************************************************************************************
	public void limpaPainelCen ( Graphics desenho )
	{
		outputPanelCen.removeAll();
		outputPanelCen.update( desenho );
	}

	//*******************************************************************************************
	public void limpaPainelDir ( Graphics desenho )
	{
		outputPanelDir.removeAll();
		outputPanelDir.update( desenho );
	}

	//*******************************************************************************************
	// METODO UTILIZADO PARA ADICIONAR UM BOTAO A UM CONTAINER DO PROGRAMA

	private JButton addAButton( String              textoBotao,
			String              textoControle,
			Container           container,
			boolean             estado,
			Color				cor,
			ControlarAplicativo controlePrograma
			) 
	{
		JButton botao;

		botao = new JButton( textoBotao );
		botao.setAlignmentX(Component.CENTER_ALIGNMENT);
		container.add(botao);
		
		botao.setBackground(cor);

		botao.setEnabled(estado);

		botao.setActionCommand( textoControle );

		botao.addActionListener( controlePrograma );

		return ( botao );
	}

	//*******************************************************************************************
	// METODO UTILIZADO PARA ADICIONAR UM BOTAO A UM CONTAINER DO PROGRAMA

	private JTextField addTextField(
			String 				textoNome,
			String 				textoControle,
			Container           container,
			ControlarAplicativo controlePrograma
			) 
	{
		JTextField text;
		JLabel label = new JLabel(textoNome);


		text = new JTextField();
		text.setAlignmentX(Component.CENTER_ALIGNMENT);
		container.add(label);
		container.add(text);

		//text.setActionCommand( textoControle );
		//text.addActionListener( controlePrograma );
		
		text.addKeyListener( controlePrograma );

		return ( text );
	}

	//*******************************************************************************************
	public void mudarBotoes() 
	{
		btAcao3.setEnabled(true);
		btAcao1.setEnabled(true);
		btSalva.setEnabled(true);
		btAbrirLzw.setEnabled(true);
		btSalvarLzw.setEnabled(true);
		btReset.setEnabled(true);
		btAcao4.setEnabled(true);
		controlePanelAcao3.setVisible(true);
		controlePanelAcao1.setVisible(true);
		controlePanelAcao2.setVisible(true);
		controlePanelAcao5.setVisible(true);
		controlePanelAcao4.setVisible(true);
	}

	//*******************************************************************************************
	// METODO PARA APRESENTAR O MENU DE ESCOLHA DE ARQUIVOS
	// 1 - PARA LEITURA
	// 2 - PARA GRAVACAO

	public String escolherArquivo ( int operacao )   
	{
		int          retorno;
		String       caminhoArquivo;
		JFileChooser arquivo;

		retorno = 0;
		arquivo = new JFileChooser(new File("."));

		// TIPO DE OPERACAO A SER REALIZADA
		switch ( operacao ) {
		case 1:
			retorno = arquivo.showOpenDialog(null);
			break;

		case 2:
			retorno = arquivo.showSaveDialog(null);
		}

		// OPERACAO
		caminhoArquivo = null;

		if(retorno == JFileChooser.APPROVE_OPTION){
			try {
				caminhoArquivo = arquivo.getSelectedFile().getAbsolutePath();
			}	catch (ArrayIndexOutOfBoundsException e) {
				System.out.println("erro: " + e);
			}
		} 

		return ( caminhoArquivo );
	}

	//*******************************************************************************************
	// METODO PARA MOSTRAR O FRAME BASICO

	public void showPanel() 
	{
		basePanel.setVisible(true);
	}

	//*******************************************************************************************
	public void ativarPainelAcao3()
	{
		controlePanelAcao3.setVisible(true);
	}

	//*******************************************************************************************
	public void desativarPainelAcao3()
	{
		controlePanelAcao3.setVisible(false);
	}

	//*******************************************************************************************
	public void ativarPainelAcao1()
	{
		controlePanelAcao1.setVisible(true);
	}

	//*******************************************************************************************
	public void desativarPainelAcao1()
	{
		controlePanelAcao1.setVisible(false);
	}

	//*******************************************************************************************
	public void iniciarGraphics()
	{
		desenhoCen = outputPanelCen.getGraphics();
		desenhoDir = outputPanelDir.getGraphics();
	}

	//*******************************************************************************************
	public Graphics getDesenhoC()
	{
		return ( desenhoCen );
	}

	//*******************************************************************************************
	public Graphics getDesenhoD()
	{
		return ( desenhoDir );
	}

	//******************************************************************************************

	public Double getText11(){
		return Double.parseDouble(txt11.getText().trim() );
	}

	//******************************************************************************************

	public int getText12(){
		return Integer.parseInt(txt12.getText().trim() );
	}

	//******************************************************************************************

	public int getText13(){
		return Integer.parseInt(txt13.getText().trim() ) ;
	}

	//******************************************************************************************

	public int getText21(){
		return Integer.parseInt(txt21.getText().trim() ) ;
	}

	//******************************************************************************************

	public int getText22(){
		return Integer.parseInt(txt22.getText().trim() ) ;
	}

	//******************************************************************************************

	public int getText31(){
		return Integer.parseInt(txt31.getText().trim() ) ;
	}

	//******************************************************************************************
	
	public void setAcao4(ArrayList<Integer> cores){
		lbAcao41.setText("R: " + cores.get(0) );
		lbAcao42.setText("G: " + cores.get(1) );
		lbAcao43.setText("B: " + cores.get(2) );
		lbAcao44.setText("Média: " + (cores.get(0) + cores.get(1) + cores.get(2) ) / 3 );
	}
	
	//******************************************************************************************
}
