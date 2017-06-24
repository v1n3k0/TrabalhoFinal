package pacoteBase.CONTROL;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

import javax.swing.JOptionPane;

import pacoteBase.VIEW.*;

public class ControlarAplicativo implements MouseListener, MouseMotionListener, KeyListener, ActionListener {

	private MontarPainelInicial pnCenario;
	private Graphics            desenhoCen, desenhoDir;
	private ControlarImagem     controleImagem;
	private String              nomeArquivoImagemDada;
	private char[][]            imagemCinza;
	private char[][]            imagemAtual;
	BufferedImage 				imgCen;
	BufferedImage 				imgDir;

	private int                 nLinImageAtual, nColImageAtual;
	private int                 nLinImageInic, nColImageInic;
	private int					nDimensaoFiltro;
	private int					corBorda;
	private byte             estadoDesenho;

	//*******************************************************************************************
	public ControlarAplicativo( )
	{
		pnCenario = new MontarPainelInicial( this );
		pnCenario.showPanel();
		estadoDesenho  = 0;
		nDimensaoFiltro = 3;
		corBorda = 0;
	}

	//*******************************************************************************************
	// METODO PARA CONTROLE DOS BOTOES DO APLICATIVO

	public void actionPerformed(ActionEvent e)
	{
		String comando, nomeArquivo;

		comando = e.getActionCommand();

		// DEFINE AMBIENTE GRAFICO
		if ( estadoDesenho == 0 ) {
			pnCenario.iniciarGraphics();
			desenhoCen = pnCenario.getDesenhoC();
			desenhoDir = pnCenario.getDesenhoD();
		}

		// ENDS THE PROGRAM
		if(comando.equals("botaoFim")) {
			System.exit(0);	
		}

		// INICIA O PROGRAMA
		if(comando.equals("botaoImagem")) {

			// LE IMAGEM SOLICITADA
			nomeArquivoImagemDada = pnCenario.escolherArquivo ( 1 );
			if ( nomeArquivoImagemDada != null ) {
				controleImagem = new ControlarImagem( nomeArquivoImagemDada, desenhoCen );
				estadoDesenho  = 1;
				imagemCinza    = controleImagem.getImagemCinza();
				imgCen = controleImagem.copia(controleImagem.getImagem() );
				nLinImageInic  = controleImagem.getNLin();
				nColImageInic  = controleImagem.getNCol();

				pnCenario.mudarBotoes();
				pnCenario.limpaPainelDir( desenhoDir );
				controleImagem.mostrarImagemBuffer ( imgCen, desenhoDir );

				nLinImageAtual = nLinImageInic;
				nColImageAtual = nColImageInic;
				imagemAtual    = controleImagem.copiarImagem ( imagemCinza, nLinImageInic, nColImageInic );
				
			}
		}
		
		if ( comando.equals( "abrirlzw" ) ) {
			descompressaoLZW();
		}

		if ( comando.equals( "botaoAcao1" ) ) {
			filtroMediana();
			filtroCannyMesclar();
		}

		if ( comando.equals( "botaoAcao3" ) )  {
			filtroMediana();
			filtroCanny();
		}

		if ( comando.equals( "botaoAcao4" ) ) {
			ruidoSalPimenta();
		}

		if ( comando.equals( "Acao31" ) ) {
			nDimensaoFiltro = 3;
		}

		if ( comando.equals( "Acao32" ) ) {
			nDimensaoFiltro = 5;
		}

		if ( comando.equals( "Acao33" ) ) {
			nDimensaoFiltro = 7;
		}
		
		if ( comando.equals( "Acao51" ) ) {
			corBorda = 0;
		}
		
		if ( comando.equals( "Acao52" ) ) {
			corBorda = 1;
		}
		
		if ( comando.equals( "Acao53" ) ) {
			corBorda = 2;
		}
		
		if ( comando.equals( "salvarlzw" ) ) {
			compressaoLZW();
		}

		if ( comando.equals( "botaoSalva" ) && estadoDesenho > 0 ) {
			nomeArquivo = pnCenario.escolherArquivo ( 2 );
			controleImagem.gravarImagem( nomeArquivo, imagemAtual, nLinImageAtual, nColImageAtual );
		}

		if ( comando.equals( "botaoReset" ) ) {
			
			if (estadoDesenho == 1) {
				
				pnCenario.limpaPainelCen( desenhoCen );
				controleImagem = new ControlarImagem( nomeArquivoImagemDada, desenhoCen);
				nLinImageAtual   = nLinImageInic;
				nColImageAtual   = nColImageInic;
				imagemAtual      = controleImagem.copiarImagem ( imagemCinza, nLinImageInic, nColImageInic );
				
				imgCen = controleImagem.copia(controleImagem.getImagem() );
				pnCenario.limpaPainelDir( desenhoDir );
				controleImagem.mostrarImagemBuffer ( imgCen, desenhoDir );

				pnCenario.ativarPainelAcao1();
				
			}else if(estadoDesenho == 2){
				
				pnCenario.limpaPainelCen( desenhoCen );
				controleImagem = new ControlarImagem( nomeArquivoImagemDada, desenhoCen, desenhoDir);
				nLinImageAtual   = nLinImageInic;
				nColImageAtual   = nColImageInic;
				imagemAtual      = controleImagem.copiarImagem ( imagemCinza, nLinImageInic, nColImageInic );
				
				imgCen = controleImagem.copia(controleImagem.getImagem() );

				pnCenario.ativarPainelAcao1();
				
			}
			
			
		}
	}

	//*******************************************************************************************
	@Override
	public void keyPressed(KeyEvent e) {

	}

	//*******************************************************************************************
	@Override
	public void keyReleased(KeyEvent e) {

	}

	//*******************************************************************************************
	@Override
	public void keyTyped(KeyEvent e) {
		String caracteres = "0123456789.";

		if(!caracteres.contains(e.getKeyChar() + "" ) )
			e.consume();
	}	

	//*******************************************************************************************
	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	//*******************************************************************************************
	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	//*******************************************************************************************
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	//*******************************************************************************************
	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	//*******************************************************************************************
	@Override
	public void mouseReleased(MouseEvent e) {
		int x, y;

		x = (int) e.getX();
		y = (int) e.getY();
		
		pnCenario.setAcao4(controleImagem.gerCores(imgCen, x, y) );

	}

	//*******************************************************************************************
	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	//*******************************************************************************************
	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	//*******************************************************************************************
	private void filtroMediana()
	{
		try{
			int tamFiltro = pnCenario.getText31();
			if(tamFiltro % 2 == 1){
				imgDir = controleImagem.mediana(imgCen, tamFiltro);
			}else{
				JOptionPane.showMessageDialog(null, "Filtro somente com tamanhos impares.", "Aviso", JOptionPane.WARNING_MESSAGE);
			}

		}catch(NumberFormatException e){
			imgDir = controleImagem.mediana(imgCen, nDimensaoFiltro);
		}


	}

	//*******************************************************************************************
	private void filtroCannyMesclar()
	{
		try {
			controleImagem.filtroCanny(imgDir, pnCenario.getText11(), pnCenario.getText13(), pnCenario.getText12() );
			controleImagem.mesclarImagem(imgCen, imgDir, corBorda);
			pnCenario.limpaPainelDir( desenhoDir );
			controleImagem.mostrarImagemBuffer(imgCen, desenhoDir);
			imgCen = controleImagem.copia(controleImagem.getImagem() );
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Insira os parametros para filtro de canny", "Aviso", JOptionPane.WARNING_MESSAGE);
			e.printStackTrace();
		}

	}
	
	//*******************************************************************************************
	private void filtroCanny()
	{
		try {
			controleImagem.filtroCanny(imgDir, pnCenario.getText11(), pnCenario.getText13(), pnCenario.getText12() );
			pnCenario.limpaPainelDir( desenhoDir );
			controleImagem.mostrarImagemBuffer(imgDir, desenhoDir);
			imgCen = controleImagem.copia(controleImagem.getImagem() );
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Insira os parametros para filtro de canny", "Aviso", JOptionPane.WARNING_MESSAGE);
			e.printStackTrace();
		}

	}

	//*******************************************************************************************
	private void ruidoSalPimenta()
	{
		try {
			controleImagem.ruido(imgDir, 255, 255, 255, pnCenario.getText21() );
			controleImagem.ruido(imgDir, 0, 0, 0, pnCenario.getText22() );
			pnCenario.limpaPainelDir( desenhoDir );
			controleImagem.mostrarImagemBuffer(imgDir, desenhoDir);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Insira os parametros para Ruido Sal e Pimenta", "Aviso", JOptionPane.WARNING_MESSAGE);
		}

	}

	//*******************************************************************************************
	
	private void compressaoLZW(){
		BufferedImage imgAux = controleImagem.getImagem();
		String nomeArquivo = pnCenario.escolherArquivo ( 2 );
		
		if(nomeArquivo != null){
			controleImagem.compressaoLZW(nomeArquivo, imgAux);
			JOptionPane.showMessageDialog(null, "Concluído a compactação LZW", "Concluído", JOptionPane.DEFAULT_OPTION);
		}
		
	}
	
	//*******************************************************************************************
	
	private void descompressaoLZW(){
		// LE IMAGEM SOLICITADA
		nomeArquivoImagemDada = pnCenario.escolherArquivo ( 1 );
		if ( nomeArquivoImagemDada != null ) {
			controleImagem = new ControlarImagem( nomeArquivoImagemDada, desenhoCen, desenhoDir );
			estadoDesenho  = 2;
			imagemCinza    = controleImagem.getImagemCinza();
			imgCen = controleImagem.copia(controleImagem.getImagem() );
			nLinImageInic  = controleImagem.getNLin();
			nColImageInic  = controleImagem.getNCol();

			pnCenario.mudarBotoes();

			nLinImageAtual = nLinImageInic;
			nColImageAtual = nColImageInic;
			imagemAtual    = controleImagem.copiarImagem ( imagemCinza, nLinImageInic, nColImageInic );

		}
		
		/*		 
			// LE IMAGEM SOLICITADA
			nomeArquivoImagemDada = pnCenario.escolherArquivo ( 1 );
			if ( nomeArquivoImagemDada != null ) {
				controleImagem = new ControlarImagem( nomeArquivoImagemDada, desenhoCen );
				estadoDesenho  = 1;
				imagemCinza    = controleImagem.getImagemCinza();
				imgCen = controleImagem.copia(controleImagem.getImagem() );
				nLinImageInic  = controleImagem.getNLin();
				nColImageInic  = controleImagem.getNCol();

				pnCenario.mudarBotoes();
				pnCenario.limpaPainelDir( desenhoDir );
				controleImagem.mostrarImagemBuffer ( imgCen, desenhoDir );

				nLinImageAtual = nLinImageInic;
				nColImageAtual = nColImageInic;
				imagemAtual    = controleImagem.copiarImagem ( imagemCinza, nLinImageInic, nColImageInic );
				
			}
		 */
		
	}


}
