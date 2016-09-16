/**
* AES class provides functions for encrytion and decryption of 64 bit hexademical input.
*
* @author  Manu Chandel
* @version 1.0
* @since   2016-09-16 
*/

public class AES{

	// public constructor for AES
	public AES(String text,String key){
		this.key=new Keys(key);
		//this.key.print();
		this.text=new Polynomial[4][4];
		this.setText(text);
		
	}

	// creates structured text
	private void setText(String text){

		int firstHalfByte;
		int secondHalfByte;

		for(int i=0;i<32;i+=8){
			for(int j=0;j<8;j+=2){
				firstHalfByte=Polynomial.hexVal(text.charAt(i+j));
				secondHalfByte=Polynomial.hexVal(text.charAt(i+j+1));

				this.text[(i>>3)][(j>>1)]=new Polynomial((firstHalfByte<<4|secondHalfByte));
			}
		}
	}

	// add round key
	private void addRoundKey(int round){

		for(int i=0;i<4;i++){
			for(int j=0;j<4;j++){
				this.text[i][j]=Polynomial.add(this.text[i][j],key.getKey(round*4+i,j));
			}
		}
	}

	// substitute Sbox
	private void subBytes(){
		
		for(int i=0;i<4;i++)
			for(int j=0;j<4;j++){
				
				int row=(this.text[i][j].get()&(15<<4))>>4;
				int col=this.text[i][j].get()&15;

				this.text[i][j].set(Sbox.getSbox(row,col));
			}
	}

	// substitute inverse Sbox
	private void invSubBytes(){
		
		for(int i=0;i<4;i++)
			for(int j=0;j<4;j++){
				
				int row=(this.text[i][j].get()&(15<<4))>>4;
				int col=this.text[i][j].get()&15;

				this.text[i][j].set(Sbox.getInvSbox(row,col));
			}
	}

	// shift rows
	private void shiftRows(){
		
		Polynomial temp[]=new Polynomial[4];
		for(int i=0;i<4;i++){

			for(int j=0;j<4;j++)
				temp[j]=this.text[j][i];

			for(int j=0;j<4;j++){
				this.text[j][i]=temp[(j+i)%4];
			}
		}
	}

	// shift rows
	private void invShiftRows(){
		
		Polynomial temp[]=new Polynomial[4];
		for(int i=0;i<4;i++){

			for(int j=0;j<4;j++)
				temp[j]=this.text[j][i];

			for(int j=0;j<4;j++){
				this.text[j][i]=temp[(j-i+4)%4];
			}
		}
	}

	// mix columns
	private void mixColumns(){

		Polynomial temp[]=new Polynomial[4];

		for(int i=0;i<4;i++){
			
			for(int j=0;j<4;j++){
				temp[j]=this.text[i][j];
			}

			for(int j=0;j<4;j++){
				this.text[i][j]=Polynomial.mul(temp[j], new Polynomial(2));
				this.text[i][j]=Polynomial.add(this.text[i][j],Polynomial.mul(new Polynomial(3),temp[(j+1)%4]));
				this.text[i][j]=Polynomial.add(this.text[i][j],temp[(j+2)%4]);
				this.text[i][j]=Polynomial.add(this.text[i][j],temp[(j+3)%4]);
			}
		}
	}

	// mix columns
	private void invMixColumns(){

		Polynomial temp[]=new Polynomial[4];

		for(int i=0;i<4;i++){
			
			for(int j=0;j<4;j++){
				temp[j]=this.text[i][j];
			}

			for(int j=0;j<4;j++){
				this.text[i][j]=Polynomial.mul(temp[j], new Polynomial(0x0E));
				this.text[i][j]=Polynomial.add(this.text[i][j],Polynomial.mul(new Polynomial(0x0B),temp[(j+1)%4]));
				this.text[i][j]=Polynomial.add(this.text[i][j],Polynomial.mul(new Polynomial(0x0D),temp[(j+2)%4]));
				this.text[i][j]=Polynomial.add(this.text[i][j],Polynomial.mul(new Polynomial(0x09),temp[(j+3)%4]));
			}
		}
	}

	
	// encrypt
	public String encrypt(){

		

		// N-1 rounds
		for(int i=0;i<9;i++){
			
			this.addRoundKey(i);
			//this.print();
			this.subBytes();
			//this.print();
			this.shiftRows();
			//this.print();
			this.mixColumns();
			//this.print();
		}

		this.addRoundKey(9);
		this.subBytes();
		this.shiftRows();
		this.addRoundKey(10);

		return this.toString();
		
		
	}

	public String decrypt(){

		
		this.addRoundKey(10);
		this.invShiftRows();
		this.invSubBytes();
		

		// N-1 rounds
		for(int i=9;i>0;i--){
			
			this.addRoundKey(i);
			//this.print();

			this.invMixColumns();
			//this.print();

			this.invShiftRows();
			//this.print();

			this.invSubBytes();
			//this.print();	
			
		}

		this.addRoundKey(0);

		return this.toString();
	}

	// convert cipher to string
	public String toString(){

		String s="";
		for(int i=0;i<4;i++){
			for(int j=0;j<4;j++){
				s+=String.format("%02X", this.text[i][j].get());
			}
		}
		return s;
	}

	// print cipher
	public void print(){
		System.out.println(this.toString());
	}


	// text in structured form
	private Polynomial text[][];

	// Key
	private Keys key;


}
