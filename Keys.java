/**
* Keys class calculates Keys by expanding given key for AES.
*
* @author  Manu Chandel
* @version 1.0
* @since   2016-09-16 
*/

public class Keys{

	public Keys(String key){

		this.keys=new Polynomial[44][4];
		this.setKeys(key.toLowerCase());
		this.expandKey();
	}

	static{

		// initialize round constants
		Keys.roundConsant=new Polynomial[11];

		// intialize round constant
		Keys.roundConsant[0]=new Polynomial(1);

		// multiply constant
		Polynomial constant=new Polynomial(2);

		// calculate all round constants
		for(int i=1;i<11;i++)
			Keys.roundConsant[i]=Polynomial.mul(Keys.roundConsant[i-1],constant);
	}

	// set initial keys
	private void setKeys(String key){

		int firstHalfByte;
		int secondHalfByte;

		for(int i=0;i<32;i+=8){
			
			for(int j=0;j<8;j+=2){
				firstHalfByte=Polynomial.hexVal(key.charAt(i+j));
				secondHalfByte=Polynomial.hexVal(key.charAt(i+j+1));

				this.keys[(i>>3)][(j>>1)]=new Polynomial((firstHalfByte<<4|secondHalfByte));
			}
		}
	}

	// key expansion
	private void expandKey(){
		
		for(int i=4;i<44;i++){

			// if i is divisible by 4
			if(i%4==0){
				
				// rotate by 1
				for(int j=0;j<4;j++){
					this.keys[i][j]=this.keys[i-1][(j+1)%4];
				}

				// substitute and xor with key[i-4]
				for(int j=0;j<4;j++){

					int row=(this.keys[i][j].get()&(15<<4))>>4;
					int column=this.keys[i][j].get()&15;

					this.keys[i][j]=Polynomial.add(this.keys[i-4][j],new Polynomial(Sbox.getSbox(row,column)));

				}

				// xor first byte with round constant
				this.keys[i][0]=Polynomial.add(this.keys[i][0],this.roundConsant[(i>>2)-1]);

			}else{

				// xor with i-1 nd i-4 word
				for(int j=0;j<4;j++){

					this.keys[i][j]=Polynomial.add(this.keys[i-1][j],this.keys[i-4][j]);
				}
			}
		}

	}

	// get key reference
	public Polynomial getKey(int i,int j){
		return this.keys[i][j];
	}
	// prints sub keys for every round
	public void print(){

		for(int i=0;i<44;i++){
			for(int j=0;j<4;j++){
				System.out.print(String.format("%02X", this.keys[i][j].get()));
			}
			
			if((i+1)%4==0)
				System.out.println();
		}
	}

	// all keys 
	private Polynomial keys[][];

	// round constants
	private static Polynomial roundConsant[];

}
