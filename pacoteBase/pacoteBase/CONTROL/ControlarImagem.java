package pacoteBase.CONTROL;

import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import javax.imageio.ImageIO;

public class ControlarImagem {

	private	char[][]      imagemCinza;
	private int           nLinImagem;
	private int           nColImagem;
	private BufferedImage imagemDada;

	// Atributos
	// Escala e magnitude para imagens
	static double MAG_SCALE = 20.0;

	// Tamanho máximo da máscara do filtro
	static int MAX_MASK_SIZE = 20;

	// Fração de pixels que estarão acima do limiar de corte superior
	double ratio = 0.1;
	int LARGURA = 0;

	//*******************************************************************************************
	public ControlarImagem( String   nomeArquivoImagemDada,
			Graphics desenho
			)
	{
		imagemDada = lerImagem ( nomeArquivoImagemDada );
		if ( imagemDada != null ) {
			mostrarImagemBuffer ( imagemDada, desenho );
			criarImagemCinza ( imagemDada );
		}
	}
	
	//*******************************************************************************************
	public ControlarImagem( String   nomeArquivoImagemDada,
			Graphics desenhoCen, 
			Graphics desenhoDir
			)
	{
		imagemDada = descompressaoLZW( nomeArquivoImagemDada );
		if ( imagemDada != null ) {
			mostrarImagemBuffer ( imagemDada, desenhoCen );
			mostrarImagemBuffer ( imagemDada, desenhoDir );
			criarImagemCinza ( imagemDada );
		}
	}
	
	//*******************************************************************************************
	// METODO PARA GERAR A IMAGEM RASTER EM NIVEIS DE CINZA A PARTIR DA IMAGEM BUFERIZADA COLORIDA

	private void criarImagemCinza ( BufferedImage imagem ) 
	{
		int    x, y, r, g, b;
		Raster imagemRasterEntrada;
		char   valorSaida;

		// DIMENSOES DA MATRIZ CINZA
		nColImagem  = imagem.getWidth(null);
		nLinImagem  = imagem.getHeight(null);
		imagemCinza = new char[nColImagem][nLinImagem];

		// DEFININDO IMAGENS INTERMEDIARIAS
		imagemRasterEntrada = imagem.getRaster();

		// CRIANDO IMAGEM CINZA
		for ( y = 0; y < nLinImagem; y++ ) {
			for ( x = 0; x < nColImagem; x++ ) {

				// LENDA DADOS DAS BANDAS DA IMAGEM DADA
				r = imagemRasterEntrada.getSample(x,y,0);  // le dado da banda 0 da imagem de entrada 

				try {
					g = imagemRasterEntrada.getSample(x,y,1);  // le dado da banda 1 da imagem de entrada 
					b = imagemRasterEntrada.getSample(x,y,2);  // le dado da banda 2 da imagem de entrada
				} catch ( ArrayIndexOutOfBoundsException e) {
					g = r;
					b = r;
				}

				//  GERANDO NIVEL DE CINZA 
				valorSaida = (char)((r+g+b)/3);
				imagemCinza[x][y] = valorSaida;
			}
		}
	}

	//******************************************************************************************
	public char[][] copiarImagem ( char[][] imagemC,
			int      nLinImg, 
			int      nColImg
			)
	{
		int      x, y;
		char[][] imagemR;

		imagemR = new char[nColImg][nLinImg];

		for ( x = 0; x < nColImg; x++ ) 
			for ( y = 0; y < nLinImg; y++ )
				imagemR[x][y] = imagemC[x][y];

		return ( imagemR );
	}

	//*******************************************************************************************
	private BufferedImage lerImagem ( String nomeArquivo )
	{
		File          arquivoImagem;
		BufferedImage imagem;

		// INICIALIZANDO VARIAVEIS
		imagem        = null;
		arquivoImagem = new File(nomeArquivo);

		// LEITURA DA IMAGEM
		try {
			imagem = ImageIO.read( arquivoImagem );
		} catch (IOException e) {
			System.out.println ( "imagem " + nomeArquivo + " nao existe");
		}

		return ( imagem );
	}

	//*******************************************************************************************
	// MOSTRAR IMAGEM BUFERIZADA

	public void mostrarImagemBuffer  ( BufferedImage imagem,
			Graphics      desenho 
			)
	{
		int imageWidth, imageHeight, x, sx, y, sy, cell, dx, dy;
		int cells[] = {0, 1, 2, 3};

		imageWidth  = imagem.getWidth(null);
		imageHeight = imagem.getHeight(null);

		for ( x = 0; x < 2; x++ ) {
			sx = x * imageWidth;
			for ( y = 0; y < 2; y++ ) {
				sy   = y * imageHeight;
				cell = cells[x*2+y];
				dx   = (cell / 2) * imageWidth;
				dy   = (cell % 2) * imageHeight;
				desenho.drawImage( imagem, dx, dy, x + imageWidth, dy + imageHeight,  
						sx, sy,  sx + imageWidth, sy + imageHeight, null );
			}
		}
	}

	//*******************************************************************************************
	// MOSTRAR IMAGEM DO TIPO MATRIZ DE BYTES

	public void mostrarImagemMatriz  ( char[][] imagemM,
			int      nLin,
			int      nCol,
			Graphics desenho 
			)
	{
		BufferedImage imagemB;

		imagemB = transformarMatriz2Buffer ( imagemM, nLin, nCol );
		desenho.drawImage( imagemB, 0, 0, nCol, nLin,  null );  
	}

	//*******************************************************************************************
	public BufferedImage transformarMatriz2Buffer ( char[][] imagemM,
			int      nLin,
			int      nCol
			)
	{
		int            x, y;
		char           valorSaida;
		WritableRaster imagemRasterSaida;
		BufferedImage  imagemB;

		imagemB           = new BufferedImage( nCol, nLin, BufferedImage.TYPE_BYTE_GRAY ); 
		imagemRasterSaida = imagemB.getRaster();

		for ( y = 0; y < nLin; y++ ) {
			for ( x = 0; x < nCol; x++ ) {
				valorSaida = imagemM[x][y];
				imagemRasterSaida.setSample( x, y, 0, valorSaida );
			}
		}

		return ( imagemB );
	}

	//*******************************************************************************************
	public void gravarImagem ( String   nomeArquivo,
			char[][] imagemM,
			int      nLin,
			int      nCol
			)
	{
		File          arquivoImagem;
		BufferedImage imagemB;

		imagemB = transformarMatriz2Buffer ( imagemM, nLin, nCol );

		// INICIALIZANDO VARIAVEIS
		arquivoImagem = new File(nomeArquivo + ".jpg" );

		// LEITURA DA IMAGEM
		try {
			ImageIO.write( imagemB, "jpg", arquivoImagem );
		} catch (IOException e) {
			System.out.println ( "imagem " + nomeArquivo + " nao existe");
		}
	}

	//*******************************************************************************************
	public char[][] getImagemCinza ( )
	{
		return ( imagemCinza );
	}

	//*******************************************************************************************
	public int getNLin()
	{
		return ( nLinImagem );
	}

	//*******************************************************************************************
	public int getNCol()
	{
		return ( nColImagem );
	}

	//*******************************************************************************************
	public BufferedImage getImagem(){
		return imagemDada;
	}

	//*******************************************************************************************
	public void setImagem(BufferedImage img){
		imagemDada = img;
	}
	
	//*******************************************************************************************
	
	public BufferedImage copia(BufferedImage original){
		ColorModel model = original.getColorModel();
		WritableRaster raster = original.copyData(null);
		BufferedImage alterada = new BufferedImage(model, raster, model.isAlphaPremultiplied(), null);
		
		return alterada;
	}

	//*******************************************************************************************
	
	public ArrayList<Integer> gerCores(BufferedImage imagem, int x, int y){
		ArrayList<Integer> cores = new ArrayList<Integer>();
		
		WritableRaster imagemlWR = imagem.getRaster();
		cores.add(imagemlWR.getSample(x, y, 0) );

		try {
			cores.add(imagemlWR.getSample(x, y, 1) );
			cores.add(imagemlWR.getSample(x, y, 2) );

		} catch (Exception e) {
			cores.add(imagemlWR.getSample(x, y, 0) );
			cores.add(imagemlWR.getSample(x, y, 0) );
		}
		
		return cores;
	}
	
	//*******************************************************************************************

	public void ruido(BufferedImage original, int r, int g, int b, int x){
		int i;
		int width = original.getWidth();
		int height = original.getHeight();
		int cor = new Color(r, g, b).getRGB();
		Random gerador = new Random();

		for(i = 0; i < x; i++){
			original.setRGB( gerador.nextInt( width ), gerador.nextInt( height ), cor);
		}
	}

	//*******************************************************************************************

	public BufferedImage mediana(BufferedImage original, int tam){
		int i, j, cor, nLargura, nAltura;
		nLargura = original.getWidth();
		nAltura = original.getHeight();
		BufferedImage alterada = new BufferedImage(original.getWidth(), original.getHeight(), BufferedImage.TYPE_BYTE_GRAY );
		WritableRaster alteradaWR = alterada.getRaster();

		//Percorre todos os pixel da imagem
		for(i = 0; i < nLargura; i++){
			for(j = 0; j < nAltura; j++){
				cor = vizinhoMediana(original, i, j, tam);
				alteradaWR.setSample(i, j, 0, cor);
			}
		}
		return alterada;
	}

	//*************************************************************************************

	private int  vizinhoMediana(BufferedImage original, int x, int y, int tam){
		int i, j, m, xi, yi, xf, yf, nLargura, nAltura;
		nLargura = original.getWidth();
		nAltura = original.getHeight();
		ArrayList<Integer> cores = new ArrayList<Integer>();
		WritableRaster originalWR = original.getRaster();

		//Metade do filtro
		m = tam / 2;
		//Inicio dos pixel vizinhos
		xi = x - m;
		yi = y - m;
		//Fim dos pixel vizinhos
		xf = x + m;
		yf = y + m;

		//Adiciona todos os pixel vizinhos
		for(i = xi; i <= xf; i++){
			for(j = yi; j <= yf; j++){
				if( i >= 0 && i < nLargura && j >= 0 && j < nAltura ){
					cores.add(getCor(originalWR, i, y) );
				}
			}
		}

		//Ordena o arrayList
		Collections.sort(cores);

		//Cor da mediana
		return cores.get( (cores.size() / 2) + 1 );
	}

	//*************************************************************************************

	private int getCor(WritableRaster imagemWR, int x, int y){
		int r, g, b;

		r = imagemWR.getSample(x, y, 0);

		try {
			g = imagemWR.getSample(x, y, 1);
			b = imagemWR.getSample(x, y, 2);

		} catch (Exception e) {
			g = r;
			b = r;
		}

		return (r + g + b) / 3;
	}

	//*************************************************************************************

	public void filtroCanny(BufferedImage imagem, double s, int inf, int sup) {

		// Define objeto BufferedImage para encapsular a imagem
		BufferedImage imagemMagnitude = null;
		int w, h, tipo;

		// Cria imagem local
		w = imagem.getWidth();
		h = imagem.getHeight();
		tipo = BufferedImage.TYPE_BYTE_GRAY;
		imagemMagnitude = new BufferedImage(w, h, tipo);

		// Chama o método do filtro
		canny(s, imagem, imagemMagnitude);

		// Define pixels de borda usando valores limiares sup e inf
		linhasBordas(sup, inf, imagem, imagemMagnitude);
	}

	//*******************************************************************************************

	private void canny(double s, BufferedImage imagem, 
			BufferedImage imagemMag) {

		int width = 0, nc, nr;
		double[][]	componenteX,	// Componente x da imagem original convolvida
		// com a função Gaussiana
		componenteY, 	// Componente y da imagem original convolvida
		// com a função Gaussiana
		derivadaX,		// Componente x da imagem convolvida 
		// (componenteX) com derivada da Gaussiana
		derivadaY;		// Componente y da imagem convolvida 
		// (componenteY) com derivada da Gaussiana
		double	funcGauss[], 			// Valores da função Gaussiana
		derivadaGauss[],	// Valores da primeira derivada da Gaussiana
		z;

		funcGauss = new double[MAX_MASK_SIZE];
		derivadaGauss = new double[MAX_MASK_SIZE];
		nc = imagem.getWidth();
		nr = imagem.getHeight();

		// Cria uma máscara do filtro Gaussiano e de sua derivada
		for (int i = 0; i < MAX_MASK_SIZE; i++) {
			funcGauss[i] = mediaGauss((double)i, s);
			if (funcGauss[i] < 0.005) {
				width = i;
				break;
			}
			derivadaGauss[i] = dGauss((double)i, s);
		}

		LARGURA = (int)width/2;

		componenteX = new double[nc][nr];
		componenteY = new double[nc][nr];

		// Convolução da imagem original com a máscara Gaussiana nas 
		// direções x e y.
		convolveImagemXY(imagem, funcGauss, width, componenteX, componenteY);

		// Convolve imagem suavizada com a derivada
		derivadaX = convolveDerivadaXY(componenteX, nr, nc, derivadaGauss, width, 
				1);
		derivadaY = convolveDerivadaXY(componenteY, nr, nc, derivadaGauss, width, 
				0);

		WritableRaster magWR = imagemMag.getRaster();
		// Cria a imagem magnitude das derivadas de x e y (gradiente)
		for (int i = 0; i < nr; i++)
			for (int j = 0; j < nc; j++) {
				z = norma(derivadaX[j][i], derivadaY[j][i]);
				magWR.setSample(j, i, 0, (int)z*MAG_SCALE);
			}

		// Suprime falsos máximos - pixels de borda deveriam ser máximo local.
		// Detalhes desta função no texto do Comunicado Técnico.
		removeFalsoMax(derivadaX, derivadaY, nr, nc, imagemMag);
	}

	//*******************************************************************************************

	// Calcula média da função Gauss
	private double mediaGauss(double x, double sigma) {
		double z;

		z = (gauss(x,sigma)+gauss(x+0.5,sigma)+gauss(x-0.5,sigma))/3.0;
		z = z/(Math.PI*2.0*sigma*sigma);
		return z;
	}

	//*******************************************************************************************

	// Calcula valor da função Gaussiana
	private double gauss(double x, double sigma) {
		double expoente;

		if (sigma == 0)
			return 0.0;
		expoente = Math.exp(((-x*x)/(2*sigma*sigma)));
		return expoente;
	}

	//*******************************************************************************************

	// Calcula valor da primeira derivada da função Gaussiana
	private double dGauss(double x, double sigma) {
		return (-x/(sigma*sigma)*gauss(x, sigma));
	}

	//*******************************************************************************************

	// Realiza a convolução separadamente nas componentes x e y da imagem.
	private void convolveImagemXY(
			BufferedImage imagem, double[] funcGauss, int width, double[][] compX, 
			double[][] compY) {

		int i1, i2, nr, nc;
		double x, y;

		nc = imagem.getWidth();
		nr = imagem.getHeight();

		Raster imR = imagem.getRaster(); 
		for (int i = 0; i < nr; i++)
			for (int j = 0; j < nc; j++) {
				x = funcGauss[0]*imR.getSample(j, i, 0);
				y = funcGauss[0]*imR.getSample(j, i, 0);
				for (int k = 1; k < width; k++) {
					i1 = (i+k)%nr; 
					i2 = (i-k+nr)%nr;
					y += funcGauss[k]*imR.getSample(j, i1, 0) + 
							funcGauss[k]*imR.getSample(j, i2, 0);
					i1 = (j+k)%nc; 
					i2 = (j-k+nc)%nc;
					x += funcGauss[k]*imR.getSample(i1, i, 0) + 
							funcGauss[k]*imR.getSample(i2, i, 0);
				}
				compX[j][i] = x; 
				compY[j][i] = y;
			}
	}

	//*******************************************************************************************

	// Realiza a convolução nas derivadas das componentes x e y 
	// da imagem convolvida.
	private double[][] convolveDerivadaXY(double[][] imagem, int nr, 
			int nc, double[] funcGauss, int width, int compXY) {

		int i1, i2;
		double x;
		double[][] componente = new double[nc][nr];

		for (int i = 0; i < nr; i++)
			for (int j = 0; j < nc; j++) {
				x = 0.0;
				for (int k = 1; k < width; k++) {
					switch (compXY){
					case 0:						// componente y
						i1 = (i+k)%nr;
						i2 = (i-k+nr)%nr;
						x += -funcGauss[k]*imagem[j][i1] + funcGauss[k]*imagem[j][i2];
						break;
					case 1:						// componente x
						i1 = (j+k)%nc; 
						i2 = (j-k+nc)%nc;
						x += -funcGauss[k]*imagem[i1][i] + funcGauss[k]*imagem[i2][i];
						break;
					}
				}
				componente[j][i] = x;
			}
		return componente;
	}

	//*******************************************************************************************

	// Suprime falsos máximos - pixels de borda deveriam ser máximo local
	// Detalhes desta função no texto do Comunicado Técnico
	private void removeFalsoMax(double[][] derivX, double[][] derivY, 
			int nr, int nc, BufferedImage imagemMag) {

		double xx, yy, grad1, grad2, grad3, grad4, gradiente, compX, compY;

		nc = imagemMag.getWidth();
		nr = imagemMag.getHeight();

		WritableRaster magWR = imagemMag.getRaster();

		for (int i = 1; i < nr - 1; i++) {
			for (int j = 1; j < nc - 1; j++) {
				magWR.setSample(j, i, 0, 0);

				// Derivadas de x e y são componentes de um vetor (gradiente)
				compX = derivX[j][i];
				compY = derivY[j][i];
				if (Math.abs(compX) < 0.01 && Math.abs(compY) < 0.01) 
					continue;
				gradiente  = norma(compX, compY);

				// Segue a direção do gradiente, vetor (compX, compY).
				// Mantém pixels da borda (máximo local).
				if (Math.abs(compY) > Math.abs(compX)) {
					// Primeiro caso: componente y é maior. Direção do gradiente é
					//								para cima ou para baixo.
					xx = Math.abs(compX)/Math.abs(compY);
					yy = 1.0;
					grad2 = norma(derivX[j][i-1], derivY[j][i-1]);
					grad4 = norma(derivX[j][i+1], derivY[j][i+1]);
					if (compX*compY > 0.0) {
						grad1 = norma(derivX[j-1][i-1], derivY[j-1][i-1]);
						grad3 = norma(derivX[j+1][i+1], derivY[j+1][i+1]);
					} else {
						grad1 = norma(derivX[j+1][i-1], derivY[j+1][i-1]);
						grad3 = norma(derivX[j-1][i+1], derivY[j-1][i+1]);
					}
				} else {
					// Segundo caso: componente x é maior. Direção do gradiente é
					//							 para esquerda ou direita.
					xx = Math.abs(compY)/Math.abs(compX);
					yy = 1.0;
					grad2 = norma(derivX[j+1][i], derivY[j+1][i]);
					grad4 = norma(derivX[j-1][i], derivY[j-1][i]);
					if (compX*compY > 0.0) {
						grad1 = norma(derivX[j+1][i+1], derivY[j+1][i+1]);
						grad3 = norma(derivX[j-1][i-1], derivY[j-1][i-1]);
					} else {
						grad1 = norma(derivX[j+1][i-1], derivY[j+1][i-1]);
						grad3 = norma(derivX[j-1][i+1], derivY[j-1][i+1]);
					}
				}

				// Calcula o valor interpolado da magnitude do gradiente.
				if ((gradiente > (xx*grad1 + (yy-xx)*grad2)) &&
						(gradiente > (xx*grad3 + (yy-xx)*grad4))) {
					if (gradiente*MAG_SCALE <= 255)
						magWR.setSample(j, i, 0, (int)gradiente*MAG_SCALE);
					else
						magWR.setSample(j, i, 0, 255);
				} else {
					magWR.setSample(j, i, 0, 0);
				}
			}
		}
	}

	//*******************************************************************************************

	// Define pixels de borda.
	private void linhasBordas(int sup, int inf, BufferedImage imagem, 
			BufferedImage imagemMag) {

		int nr, nc;

		nc = imagem.getWidth();
		nr = imagem.getHeight();
		WritableRaster imWR = imagem.getRaster();
		Raster imR = imagem.getRaster(); 
		Raster magR = imagemMag.getRaster(); 

		for (int i = 0; i < nr; i++)
			for (int j = 0; j < nc; j++)
				imWR.setSample(j, i, 0, 0);

		if (sup < inf) {
			estimaLimiar(imagemMag, sup, inf);
		}

		// Para cada borda com magnitude acima do limiar superior, desenha
		// pixels de borda que estão acima do limiar inferior.
		for (int i = 0; i < nr; i++)
			for (int j = 0; j < nc; j++)
				if (magR.getSample(j, i, 0) >= sup)
					trace(i, j, inf, imagem, imagemMag);

		// Torna a borda em preto
		for (int i = 0; i < nr; i++)
			for (int j = 0; j < nc; j++)
				if (imR.getSample(j, i, 0) == 0)
					imWR.setSample(j, i, 0, 255);
				else
					imWR.setSample(j, i, 0, 0);
	}

	//*******************************************************************************************

	// Estima o limiar superior
	private void estimaLimiar(BufferedImage imagemMag, int hi, int inf) {

		int histograma[], count, nr, nc, i, j, k;

		nc = imagemMag.getWidth();
		nr = imagemMag.getHeight();
		histograma = new int[256];
		Raster magR = imagemMag.getRaster(); 

		// Histograma da imagem
		for (k = 0; k < 256; k++)
			histograma[k] = 0;

		for (i = LARGURA; i < nr - LARGURA; i++)
			for (j = LARGURA; j < nc - LARGURA; j++)
				histograma[magR.getSample(j, i, 0)]++;

		// O limiar superior deveria ser maior que 80 ou 90% dos pixels 
		j = nr;
		if (j < nc)
			j = nc;
		j = (int)(0.9*j);
		k = 255;

		count = histograma[255];
		while (count < j) {
			k--;
			if (k < 0)
				break;
			count += histograma[k];
		}

		hi = k;
		i = 0;
		while (histograma[i] == 0)
			i++;
		inf = (int)(hi+i)/2;
	}

	//*******************************************************************************************

	// Traça, recursivamente, os pixels da borda.
	private int trace(int i, int j, int inf, BufferedImage imagem,
			BufferedImage imagemMag) {

		int n, m;
		int flag = 0;

		Raster magR = imagemMag.getRaster();
		Raster imR = imagem.getRaster();
		WritableRaster imWR = imagem.getRaster();

		if (imR.getSample(j, i, 0) == 0) {
			imWR.setSample(j, i, 0, 255);
			flag = 0;
			for (n = -1; n <= 1; n++) {
				for(m = -1; m <= 1; m++) {
					if (i == 0 && m == 0)
						continue;
					if ((range(imagemMag, i+n, j+m) == 1) && 
							(magR.getSample(j+m, i+n, 0)) >= inf)
						if (trace(i+n, j+m, inf, imagem, imagemMag) == 1)	{
							flag = 1;
							break;
						}
				}
				if (flag == 1)
					break;
			}
			return 1;
		}
		return 0;
	}

	//*******************************************************************************************

	// Certifica que um pixel pertence à imagem
	private int range(BufferedImage imagem, int i, int j) {
		int nc, nr;

		nc = imagem.getWidth();
		nr = imagem.getHeight();
		if ((i < 0) || (i >= nr))
			return 0;
		if ((j < 0) || (j >= nc))
			return 0;
		return 1;
	}

	//*******************************************************************************************

	// Calcula a norma ou magnitude, do gradiente.
	private double norma(double x, double y) {
		return Math.sqrt(x*x + y*y);	
	}

	//*******************************************************************************************
	//Faz união da imagem original com imagem das bordas
	public void mesclarImagem(BufferedImage original, BufferedImage imgborda, int cor){
		int x, y;
		int nl = original.getHeight();
		int nc = original.getWidth();
		
		WritableRaster originalWR = original.getRaster();
		WritableRaster imgBordaWR = imgborda.getRaster();
				
		for(y = 0; y < nl; y++){
			for (x = 0; x < nc; x++) {
				if(imgBordaWR.getSample(x, y, 0) == 0 ){
					
					try {
						originalWR.setSample(x, y, 0, 0);
						originalWR.setSample(x, y, 1, 0);
						originalWR.setSample(x, y, 2, 0);
					} catch (Exception e) {
						e.printStackTrace();
					}
					
					originalWR.setSample(x, y, cor, 255);
				}
			}
		}
		
	}
	
	//*******************************************************************************************
	//Compressão de imagem
	public void compressaoLZW(String nomeArquivo, BufferedImage imagem){
		int x, y, nLargura, nAltura;
		ArrayList<String> dicionario = new ArrayList<String>();
		ArrayList<Integer> codigo = new ArrayList<Integer>();
		nLargura = imagem.getWidth();
		nAltura = imagem.getHeight();
		WritableRaster imagemWR = imagem.getRaster();
		String 	c = "",
				I = "";		

		//iniciar o dicionario
		iniciarDicionario(dicionario);
				
		//Codificação da imagem
		for(y = 0; y < nAltura; y++){
			for(x = 0; x < nLargura; x++){
				c = getCores(imagemWR, x, y);

				if(dicionario.indexOf(I.concat(c) ) != -1){
					I = I.concat(c);
				}else{
					codigo.add(dicionario.indexOf(I) );
					dicionario.add(I.concat(c) );
					I = c;
				}

			}
		}
		codigo.add(dicionario.indexOf(I) );
		
		//Salva a codigo da imagem
		salvarArquivo(nomeArquivo, codigo, nLargura, nAltura);

	}

	//*******************************************************************************************
	//Calcula media das cores do pixel
	private String getCores(WritableRaster imagemWR, int x, int y){
		int r, g, b;

		r = imagemWR.getSample(x, y, 0);

		try {
			g = imagemWR.getSample(x, y, 1);
			b = imagemWR.getSample(x, y, 2);

		} catch (Exception e) {
			g = r;
			b = r;
		}

		return Integer.toString( (r + g + b) / 3) + ";";

	}

	//*******************************************************************************************
	//Inicia o dicionario com todos os possiveis valores primario
	private void iniciarDicionario(ArrayList<String> dicionario){
		for (int i = 0; i < 256; i++) {
			dicionario.add(Integer.toString(i) + ";" ); //Separador ";"
		}
	}

	//*******************************************************************************************
	//Salva o arquivo codificado
	private void salvarArquivo(String nomeArquivo, ArrayList<Integer> saida, int nLargura, int nAltura){
		try {
			File arquivo = new File(nomeArquivo + ".lzw");
			FileOutputStream gravarArquivo = new FileOutputStream(arquivo);
			DataOutputStream  oos = new DataOutputStream (gravarArquivo);
			
			//Escreve a dimensao da imagem
			oos.writeInt(nLargura);
			oos.writeInt(nAltura);

			//escreve codigo da imagem
			for (int i = 0; i < saida.size(); i++) {
				oos.writeInt(saida.get(i) );
			}			

			oos.flush();
			oos.close();
			gravarArquivo.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	//*******************************************************************************************
	//Metodo para descompressão da imagem
	public BufferedImage descompressaoLZW(String nomeArquivo){
		ArrayList<String> dicionario = new ArrayList<String>();
		ArrayList<String> saida = new ArrayList<String>();
		ArrayList<Integer> codigo = new ArrayList<Integer>();		
		int cw, pw, i;
		String p, c;
		Integer[] dimensao = new Integer[2];

		//iniciar o dicionario
		iniciarDicionario(dicionario); 

		//Abrir o arquivo
		abrirArquivo(nomeArquivo, codigo, dimensao);
		
		//Descodifica a imagem
		cw = codigo.get(0);
		saida.add(dicionario.get(cw) );
				
		for(i = 1; i < codigo.size(); i++){
			pw = cw;
			cw = codigo.get(i);

			if(dicionario.size() > cw){
				saida.add(dicionario.get(cw) );
				p = dicionario.get(pw);
				c = primeiroCaracter(dicionario, cw);
				dicionario.add(p.concat(c) );
			}else{
				p = dicionario.get(pw);
				c = dicionario.get(pw);
				dicionario.add(concatena(p, c) );
				saida.add(concatena(p, c) );
			}
		}
		
		//Retorna imagem codifica
		return transformarArray(saida, dimensao[1], dimensao[0]);

	}

	//*******************************************************************************************
	//Pega o 1º valor da String
	private String primeiroCaracter(ArrayList<String> dicionario, int cw){
		String strings[] = dicionario.get(cw).split(";");
		return strings[0] + ";";
	}

	//*******************************************************************************************
	//Concatena p com 1º valor de c
	private String concatena(String p, String c){
		String strings[] = c.split(";");
		return p + strings[0] + ";";
	}

	//*******************************************************************************************
	
	
	//Faz a leitura do arquivo codificado
	private void abrirArquivo(String nomeArquivo, ArrayList<Integer> codigo, Integer[] dimensao){
		try{
			File arquivo = new File(nomeArquivo);
			FileInputStream lerArquivo = new FileInputStream(arquivo);
			DataInputStream dis = new DataInputStream(lerArquivo);
			
			//Dimensão da imagem
			dimensao[0] = dis.readInt();
			dimensao[1] = dis.readInt();
			
			//Leiturada imagem codificada
			while( dis.available() > 0 ){
				codigo.add( dis.readInt() );
			}

			dis.close();
			lerArquivo.close();

		}catch(Exception e){
			e.printStackTrace();
		}

	}

	//*******************************************************************************************
	
	public BufferedImage transformarArray ( ArrayList<String> saida, int nLin, int nCol){
		int x = 0, y = 0, i, j;
		WritableRaster imagemRasterSaida;
		BufferedImage  imagemB;

		imagemB           = new BufferedImage( nCol, nLin, BufferedImage.TYPE_3BYTE_BGR ); 
		imagemRasterSaida = imagemB.getRaster();

		for ( i = 0; i < saida.size(); i++ ){
			String cores[] = saida.get(i).split(";");

			for (j = 0; j < cores.length; j++) {
				if(!cores[j].equals("")){

					imagemRasterSaida.setSample( x, y, 0, Integer.parseInt(cores[j]) );
					imagemRasterSaida.setSample( x, y, 1, Integer.parseInt(cores[j]) );
					imagemRasterSaida.setSample( x, y, 2, Integer.parseInt(cores[j]) );

					x++;
					if(x == nCol){
						x = 0;
						y++;
					}

				}
			}
		}
		return ( imagemB );
	}

	//*******************************************************************************************

}
