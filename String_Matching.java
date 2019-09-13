/*Utkarsh Mehta
 COSC  -  5334 - Design & Analysis of algorithms
 Fall-2017
 Texas A & M University, Corpus Christi

I have  implemented Robin Kalp, Knuth Morris Pratt and BoyerMoore . For easy access for the user , 
I have integrated these three algorithms together. So , When we compile and run the JAVA code, 
the program asks user to enter the pattern/text/word to search in the given Text file. 
The code takes the input string which user enters and traverses all through the code going into three different functions for all three implemented algorithms. 
I haven’t converted the characters into upper case or lower case and made use of the original text file as it was provide and no modifications. Therefore , it doesn't matter
whether user enters upper case or lower case, the code runs for both(upper and lower case) of them.  
I have used Bad character ,good suffix in my code for Boyer Moore algorithm.

The program outputs the number of character comparision done to find that particular word entered by the user in the text file. This gives the efficiency of the algorithms.




Created by UTKARSH MEHTA on 15/November/2017.

Copyright � 2018 Demo. All rights reserved.
utkarshmehta93@gmail.com

*/

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;


public class Merged1 {
	static Scanner sc = new Scanner(System.in);
	@SuppressWarnings("static-access")
	public static void main(String[] args) throws IOException 
	{
		int q=101;
		String text = new String(Files.readAllBytes(Paths.get("D:/UTKARSH.txt")	));
		System.out.println("\nWELCOME TO COMPARISION OF STRING MATCHING SCHEME \n");
	     System.out.println("\nCOSC 5334 ");
	     System.out.println("UTKARSH MEHTA: A04136342 \t \t Instructor:     Dr. Dulal Chandra Kar\n" );   

		System.out.println("Enter the string you want to search");
		String input = sc.nextLine();
		
		System.out.println("---------------Rabin karp output--------------------------------------");
		new Robin_Karp().search(input, text, q); //CALLING ROBIN KALP ALGO
		System.out.println("\n---------------Knuth Morris Pratt output--------------------------------------");
		new KMP_String_Matching().KMPSearch(input, text);
		
		System.out.println("\n---------------BOYRE MOORE output--------------------------------------");
		new BoyerMoore().search(text.toCharArray(), input.toCharArray());    
	      
	}
	
}
//---------------------------------------------------------ROBIN KARP ALGO-------------------------------------------------------
 class Robin_Karp
{
    // d is the number of characters in input alphabet
    public final static int d = 256;
     
    /* pat -> pattern
        txt -> text
        q -> A prime number
    */
    static void search(String pat, String txt, int q)
    {
    	int count=0;
        int M = pat.length();
        int N = txt.length();
        int i, j;
        int p = 0; // hash value for pattern
        int t = 0; // hash value for txt
        int h = 1;
     
        // The value of h would be "pow(d, M-1)%q"
        for (i = 0; i < M-1; i++)
            h = (h*d)%q;
     
        // Calculate the hash value of pattern and first
        // window of text
        for (i = 0; i < M; i++)
        {
            p = (d*p + pat.charAt(i))%q;
            t = (d*t + txt.charAt(i))%q;
        }
     
        // Slide the pattern over text one by one
        for (i = 0; i <= N - M; i++)
        {
     
            // Check the hash values of current window of text
            // and pattern. If the hash values match then only
            // check for characters on by one
            if ( p == t )
            {
                /* Check for characters one by one */
            	
                for (j = 0; j < M; j++)
                {
                	count++;
                    if (txt.charAt(i+j) != pat.charAt(j))
                    {
                    	count++;
                        break;
                    }
                }
     
                // if p == t and pat[0...M-1] = txt[i, i+1, ...i+M-1]
                if (j == M)
                {
                    System.out.println("Pattern found at index " + i);
                    break;
                }
            }
     
            // Calculate hash value for next window of text: Remove
            // leading digit, add trailing digit
            if ( i < N-M )
            {
                t = (d*(t - txt.charAt(i)*h) + txt.charAt(i+M))%q;
     
                // We might get negative value of t, converting it
                // to positive
                if (t < 0)
                t = (t + q);
            }
        }
        System.out.println("Number of comparsions done by 'RABIN KALP is'" + (count));
    }
     
}  /* Driver program to test above function */
 
 //----------------------------------------------------------------------Knuth Morris Pratt ALGO----------------------------------
 class KMP_String_Matching
 {
	static int count=0;
     void KMPSearch(String pat, String txt)
     {
    	 
         int M = pat.length();
         int N = txt.length();
  
         // create lps[] that will hold the longest
         // prefix suffix values for pattern
         int lps[] = new int[M];
         int j = 0;  // index for pat[]
  
         // Preprocess the pattern (calculate lps[]
         // array)
         computeLPSArray(pat,M,lps);
  
         int i = 0;  // index for txt[]
         while (i < N)
         {
             if (pat.charAt(j) == txt.charAt(i))
             {
            	count++;
                 j++;
                 i++;
             }
             if (j == M)
             {
                 System.out.println("Found pattern "+
                               "at index " + (i-j));
                 j = lps[j-1];
                 break;
             }
  
             // mismatch after j matches
             else if (i < N && pat.charAt(j) != txt.charAt(i))
             {
                 // Do not match lps[0..lps[j-1]] characters,
                 // they will match anyway
            	count++;
                 if (j != 0)
                     j = lps[j-1];
                 else
                     i = i+1;
             }
         }
         System.out.println("Comparsions done by Knuth Morris Pratt is "+ count);
     }
  
     void computeLPSArray(String pat, int M, int lps[])
     {
         // length of the previous longest prefix suffix
         int len = 0;
         int i = 1;
         lps[0] = 0;  // lps[0] is always 0
  
         // the loop calculates lps[i] for i = 1 to M-1
         while (i < M)
         {
             if (pat.charAt(i) == pat.charAt(len))
             {
            	count++;
                 len++;
                 lps[i] = len;
                 i++;
             }
             else  // (pat[i] != pat[len])
             {
                 // This is tricky. Consider the example.
                 // AAACAAAA and i = 7. The idea is similar 
                 // to search step.
            	count++;
                 if (len != 0)
                 {
                     len = lps[len-1];
  
                     // Also, note that we do not increment
                     // i here
                 }
                 else  // if (len == 0)
                 {
                     lps[i] = len;
                     i++;
                 }
             }
         }
       
     }
     
     // Driver program to test above function
 }
 
 /* Program for Bad Character Heuristic of Boyer 
 Moore String Matching Algorithm */
 class BoyerMoore {
		/* C program for Boyer Moore Algorithm with 
		Good Suffix heuristic to find pattern in
		given text string */

		
		static int cnt=0;
		static int r=0;
		// preprocessing for strong good suffix rule
		static void preprocess_strong_suffix(int[] shift, int[] bpos,
										char[] pat, int m)
		{
			// m is the length of pattern 
			int i=m, j=m+1;
			bpos[i]=j;

			while(i>0)
			{
				/*if character at position i-1 is not equivalent to
				character at j-1, then continue searching to right
				of the pattern for border */
				//cnt++;
				while(j<=m && pat[i-1] != pat[j-1])
				{
					/* the character preceding the occurence of t in 
					pattern P is different than mismatching character in P, 
					we stop skipping the occurences and shift the pattern
					
					from i to j */
					if (shift[j]==0)
						shift[j] = j-i;

					//Update the position of next border 
					j = bpos[j];
				}
				/* p[i-1] matched with p[j-1], border is found.
				store the beginning position of border */
				i--;j--;
				bpos[i] = j; 
			}
		}

		//Preprocessing for case 2
		static void preprocess_case2(int[] shift, int[] bpos,
							char[] pat, int m)
		{
			int i, j;
			j = bpos[0];
			for(i=0; i<=m; i++)
			{
				/* set the border postion of first character of pattern
				to all indices in array shift having shift[i] = 0 */
				if(shift[i]==0)
					shift[i] = j;

				/* suffix become shorter than bpos[0], use the position of 
				next widest border as value of j */
				if (i==j)
					j = bpos[j];
			}
		}

		/*Search for a pattern in given text using
		Boyer Moore algorithm with Good suffix rule */
		static void search(char[] text, char[] pat)
		{
			// s is shift of the pattern with respect to text
			int s=0, j;
			int m = pat.length;
			int n = text.length;
	//int[] bpos = new int[];
			int[] bpos = new int[m+1];
			int[] shift = new int[m+1];
			//initialize all occurence of shift to 0
			for(int i=0;i<m+1;i++) shift[i]=0;

			//do preprocessing
			preprocess_strong_suffix(shift, bpos, pat, m);
			preprocess_case2(shift, bpos, pat, m);
		    //cnt++;

			while(s <= n-m)
			{

				j = m-1;

				/* Keep reducing index j of pattern while characters of
					pattern and text are matching at this shift s*/
				//cnt++;
				while(j >= 0 && pat[j] == text[s+j])
					{
					    j--;
					    cnt++;
					    
					}
					
				/* If the pattern is present at current shift, then index j
					will become -1 after the above loop */
				if (j<0)
				{
					
					System.out.println("pattern occurs at shift = "+s);
					s += shift[0];
					break;
					
				}
				else
					/*pat[i] != pat[s+j] so shift the pattern
					shift[j+1] times */
					{
					    s += shift[j+1];
					    //cnt++;
					}
			
			    r++;
			}
			r=r/pat.length;
System.out.println("Number of comparisons done by Boyer Moore is"+ (cnt+r+1));
		}
 }


	   
	   
    
