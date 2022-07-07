// This project is created by Group X for calculating MARKOV MATRIX 
    import java.util.Random;
    import java.util.Scanner;
    import java.io.BufferedReader;
    import java.io.FileReader;

class TPM_Generator{
    public static void main(String[] args){

        Random roll = new Random();
        Scanner S = new Scanner(System.in);
        int Sample = 0;

        System.out.println("\nPress 1 for Random Sample");
        System.out.println("Press 2 for User Input Sample");
        System.out.println("Press 3 for Importing from a csv file");
        System.out.print("\nEnter your Choice ! ");
        int choice = S.nextInt();
        if(choice>3){
            System.out.println("You have entered wrong choice !");
            S.close();
            return;
        }

        System.out.print("\nHow many samples you want ? ");
        int max = S.nextInt()   ;
        int arr[] = new int[max];

        if (choice==1) {
            System.out.print("\nHow many sample spaces you want : ");
            Sample = S.nextInt();
            for(int i=0; i<max; i++){
                arr[i] = roll.nextInt(Sample)+1;
            }
        } 
        else{
            if (choice==2) {
                System.out.print("\nHow many sample spaces you want : ");
                Sample = S.nextInt();
                for(int i=0; i<max; i++){
                    System.out.print("Enter Sample "+(i+1)+" : ");
                    arr[i] = S.nextInt();
                }
            } else {
                System.out.println("\nPress 1 for data of difference between Closing and Opening price of same day");
                System.out.println("Press 2 for data of difference between Opening prices of consecutive days");
                System.out.println("Press 3 for data of difference between Closing prices of consecutive days ");
                System.out.print("\nEnter your Choice ! ");
                int column=S.nextInt();
                if(column>3){
                    System.out.println("\nYou have entered wrong choice !");
                    S.close();
                    return;
                }
                System.out.println("Important : ");
                System.out.println("1 = Difference between Closing and Opening is less than -3%");
                System.out.println("2 = Difference between Closing and Opening is between -2% and -3%");
                System.out.println("3 = Difference between Closing and Opening is between -1% and -2%");
                System.out.println("4 = Difference between Closing and Opening is between 0% and -1%");
                System.out.println("5 = Difference between Closing and Opening is between 1% and 0%");
                if(column==1){
                    System.out.println("6 = Difference between Closing and Opening is more than 1%");
                    Sample+=6;
                } else{
                    if(column==2){
                        System.out.println("6 = Difference between Closing and Opening is between 2% and 1%");   
                        System.out.println("7 = Difference between Closing and Opening is more than 2%");
                        Sample+=7;   
                    }
                    else{
                            System.out.println("6 = Difference between Closing and Opening is between 2% and 1%");   
                            System.out.println("7 = Difference between Closing and Opening is more than 2%");  
                            Sample+=7; 
                    }
                }

                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                String file = "Data_Final.csv";
                String line;
                int i=0;
                try (BufferedReader br = new BufferedReader(new FileReader(file))){
                    while (i<max) {
                        line=br.readLine();
                        String[] cols = line.split(",");
                        arr[i]=Integer.parseInt(cols[(column*3)+2]);    
                        i++;                    
                    }                    
                } catch (Exception e) {
                    System.out.println("File not found!");
                    System.out.println("Please keep code and data-file in same folder");
                    S.close();
                    return;
                }
            }
        }
        
        
        double Initial_prob[] = new double[Sample];
        Show_Random(arr,max);
        Initial_prob=Initial(arr,max,Sample);

        double TPM[][] = new double[Sample][Sample];
        TPM=Create_TPM(arr, max, Sample);
        Show_Matrix(TPM, Sample);

        double Future[][] = new double[Sample][Sample];
        System.out.print("\nAfter how many period later result you want ? ");
        int days=S.nextInt();
        Future=Future_Values(TPM, days, Sample);
        Show_Matrix(Future, Sample);

        System.out.print("\nWhich state probability you want ? ");
        int state=S.nextInt();
        if (state<=Sample) {
            State_Calc(Future, Initial_prob, state, Sample, max);
        } 
        else {
            System.out.println("You have entered wrong choice !");
        }
        

        S.close();
    }

    // Showing Random Values

    static void Show_Random(int[] arr, int max){

        System.out.println("\nSamples : ");
        for(int i=0; i<max; i++){
            System.out.print(arr[i]+" ");
        }
        System.out.println("");
        
    }
    
    // Ṣhowing Initial Probability

    static double[] Initial(int[] arr, int max, int Sample){
        double Initial_Array[]=new double[Sample];
        System.out.print("\nInitial Probability of each value : ");
        for(int i=1; i<=Sample; i++){
            double Prob=0;
            for(int j=0; j<max; j++){
                if(arr[j] == i){
                    Prob = Prob+1;
                }
            }
            System.out.print("\t");
            System.out.printf("%.2f", Prob*100/max);
            Initial_Array[i-1]=Prob;
        }
        System.out.println("");
        return Initial_Array;
    }
    
    // Creating TPM Matrix

    static double[][] Create_TPM(int[] arr, int max, int Sample){

        double T[][] = new double[Sample][Sample];

        for(int i=0; i<Sample; i++){
            for(int j=0; j<Sample; j++){
                T[i][j]=0;
            }
        }
    
        for(int i=0; i<max-1; i++){
            int a = arr[i];
            int b = arr[i+1];
            T[a-1][b-1]=T[a-1][b-1]+1;
        }

        for(int i=0; i<Sample; i++){
            double sum = 0;
            for(int j=0; j<Sample; j++){
                sum=sum+T[i][j];
            }
            for(int j=0; j<Sample; j++){
                T[i][j]=T[i][j]/sum;
            }
        }

        return T;
    }

    static void Show_Matrix(double[][] TPM, int Sample){

        System.out.println("\nTPM table:");
        for(int i=0; i<Sample; i++){
            for(int j=0; j<Sample; j++){
                System.out.printf("%.2f", TPM[i][j]*100);
                System.out.print("\t");
            }
            System.out.println("");
        }

    }
    
    // Creating TPM matrix after n period

    static double[][] Future_Values(double[][] arr1, int days, int Sample){

        double arr2[][] = new double[Sample][Sample];
        arr2 = arr1;
        while(days>1)
        {
            arr2 = Multiply(arr1, arr2, Sample);
            days-=1;
        }
        return arr2;
    }

    // Multiplication of Matrices

    static double[][] Multiply(double[][] arr1, double[][] arr2, int Sample){

        double product[][] = new double[Sample][Sample];
        for(int i=0; i<Sample; i++){
            for(int j=0; j<Sample; j++){
                for(int k=0; k<Sample; k++){
                    product[i][j]+=arr1[i][k]*arr2[k][j];
                }
            }            
        }
        return product;
    }

    // Calculating State Probability

    static void State_Calc(double[][] arr1, double[] arr, int state, int Sample, int max){

        double result[][] = new double[1][Sample];
        for (int i = 0; i < Sample; i++) {
            result[0][i]=0;
        }
        for(int i=0; i<Sample; i++){    
                for(int k=0; k<Sample; k++){
                    result[0][i]=result[0][i]+(arr[i]*arr1[i][k]);
                }         
        }

        System.out.print("\nState Matrix : [");
        for(int i=0; i<Sample; i++){
            System.out.printf("%.2f", (result[0][i]*100/max));
            System.out.print("\t");
        }
        System.out.println("]");

        for(int i=0; i<Sample; i++){
            if(i+1==state){
                System.out.print("\nProbability of state "+(i+1)+" is = ");
                System.out.printf("%.2f", (result[0][i]*100/max));
                System.out.println("");
                System.out.println("");
            }
        }
    }
}