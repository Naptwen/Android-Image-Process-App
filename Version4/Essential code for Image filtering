    public void Prewitt_mask(){
        Mask_x = new int[][]{{-1, 0, 1}, {-1, 0, 1}, {-1, 0, 1}};
        Mask_y = new int[][]{{-1, -1, -1}, {0, 0, 0}, {1, 1, 1}};
    }

    public void Sobel_mask(){
        Log.d(TAG, "SOBEL MASK");
        Mask_x = new int[][]{{-1, 0, 1}, {-2, 0, 2}, {-1, 0, 1}};
        Mask_y = new int[][]{{-1, -2, -1}, {0, 0, 0}, {1, 2, 1}};
    }

    public void Roberts_mask(){
        Mask_x = new int[][]{{1, 0, 0}, {0, -1, 0}, {0, 0, 0}};
        Mask_y = new int[][]{{0, 0, 0}, {0, -1, 0}, {1, 0, 0}};
    }

    public void Laplacian_mask(){
        Mask_x = new int[][]{{0, -1, 0}, {-1, 4,-1}, {0, -1, 0}};
        Mask_y = new int[][]{{0, 1, 0}, {1, 4,1}, {0, 1, 0}};;
    }
    //1 Set Pixel List (col to row)
    public void GetPIxel(Bitmap bitmap){
        Log.d(TAG, "Get Pixel working");
        Pixel_list = new ArrayList<>();
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        for (int j = 0; j < h; j++) {
        for (int i = 0; i < w; i++) {
                int my_pixel = bitmap.getPixel(i, j);
                int[] temp_pixel = {i,j,((my_pixel>>16) & 0xff),((my_pixel>>8) & 0xff),my_pixel & 0xff};
                Pixel_list.add(temp_pixel);
            }}
    }
    //2 Set Pixel_blur_list {Pixel_list need}
    public void Blur() {
        Log.d(TAG, "Blur working");
        if(Pixel_list != null) {
            int[][] Gaussian_matrix = new int[][]{{1, 4, 7, 4, 1}, {4, 16, 26, 16, 4}, {7, 26, 41, 26, 7}, {4, 16, 26, 16, 4}, {1, 4, 7, 4, 1}}; //Gaussian filter
            int size = Pixel_list.size();
            int last_x = Pixel_list.get(size - 1)[0];
            int last_y = Pixel_list.get(size - 1)[1];
            int last_x2 = Pixel_list.get(size - 1)[0] - 1;
            int last_y2 = Pixel_list.get(size - 1)[1] - 1;
            int w = last_x + 1;
            Pixel_blur_list = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                int[] sum = new int[]{Pixel_list.get(i)[2],Pixel_list.get(i)[3],Pixel_list.get(i)[4]};
                int avg_R = Pixel_list.get(i)[2];
                int avg_G = Pixel_list.get(i)[3];
                int avg_B = Pixel_list.get(i)[4];
                if (Pixel_list.get(i)[0] != 0 && Pixel_list.get(i)[1] != 0 && Pixel_list.get(i)[0] != last_x && Pixel_list.get(i)[1] != last_y
                        && Pixel_list.get(i)[0] != 1 &&Pixel_list.get(i)[1] != 1 && Pixel_list.get(i)[0] != last_x2 && Pixel_list.get(i)[1] != last_y2) {
                    /*
                    00 |   01   |       02       | 03 | 04
                    10 |   11   |       12       | 13 | 14
                    20 |   21   |     current    | 23 | 24
                    30 |   31   |       32       | 33 | 34
                    40 |   41   |       42       | 43 | 44
                    */
                    int pixel_00 = i - w * 2 - 2;
                    int pixel_01 = i - w * 2 - 1;
                    int pixel_02 = i - w * 2;
                    int pixel_03 = i - w * 2 + 1;
                    int pixel_04 = i - w * 2 + 2;

                    int pixel_10 = i - w - 2;
                    int pixel_11 = i - w - 1;
                    int pixel_12 = i - w;
                    int pixel_13 = i - w + 1;
                    int pixel_14 = i - w + 2;

                    int pixel_20 = i - 2;
                    int pixel_21 = i - 1;
                    // pixel_22 is current pixel position
                    int pixel_23 = i + 1;
                    int pixel_24 = i + 2;

                    int pixel_30 = i + w - 2;
                    int pixel_31 = i + w - 1;
                    int pixel_32 = i + w;
                    int pixel_33 = i + w + 1;
                    int pixel_34 = i + w + 2;

                    int pixel_40 = i + w * 2 - 2;
                    int pixel_41 = i + w * 2 - 1;
                    int pixel_42 = i + w * 2;
                    int pixel_43 = i + w * 2 + 1;
                    int pixel_44 = i + w * 2 + 2;

                    sum[0] = 0;
                    sum[1] = 0;
                    sum[2] = 0;
                    for (int k = 2; k < 5; k++) {
                        int[] matrix_pixel = new int[25];
                        matrix_pixel[0] = Pixel_list.get(pixel_00)[k] * Gaussian_matrix[0][0];
                        matrix_pixel[1] = Pixel_list.get(pixel_01)[k] * Gaussian_matrix[0][1];
                        matrix_pixel[2] = Pixel_list.get(pixel_02)[k] * Gaussian_matrix[0][2];
                        matrix_pixel[3] = Pixel_list.get(pixel_03)[k] * Gaussian_matrix[0][3];
                        matrix_pixel[4] = Pixel_list.get(pixel_04)[k] * Gaussian_matrix[0][4];

                        matrix_pixel[5] = Pixel_list.get(pixel_10)[k] * Gaussian_matrix[1][0];
                        matrix_pixel[6] = Pixel_list.get(pixel_11)[k] * Gaussian_matrix[1][1];
                        matrix_pixel[7] = Pixel_list.get(pixel_12)[k] * Gaussian_matrix[1][2];
                        matrix_pixel[8] = Pixel_list.get(pixel_13)[k] * Gaussian_matrix[1][3];
                        matrix_pixel[9] = Pixel_list.get(pixel_14)[k] * Gaussian_matrix[1][4];

                        matrix_pixel[10] = Pixel_list.get(pixel_20)[k] * Gaussian_matrix[2][0];
                        matrix_pixel[11] = Pixel_list.get(pixel_21)[k] * Gaussian_matrix[2][1];
                        matrix_pixel[12] = Pixel_list.get(i)[k]        * Gaussian_matrix[2][2];
                        matrix_pixel[13] = Pixel_list.get(pixel_23)[k] * Gaussian_matrix[2][3];
                        matrix_pixel[14] = Pixel_list.get(pixel_24)[k] * Gaussian_matrix[2][4];

                        matrix_pixel[15] = Pixel_list.get(pixel_30)[k] * Gaussian_matrix[3][0];
                        matrix_pixel[16] = Pixel_list.get(pixel_31)[k] * Gaussian_matrix[3][1];
                        matrix_pixel[17] = Pixel_list.get(pixel_32)[k] * Gaussian_matrix[3][2];
                        matrix_pixel[18] = Pixel_list.get(pixel_33)[k] * Gaussian_matrix[3][3];
                        matrix_pixel[19] = Pixel_list.get(pixel_34)[k] * Gaussian_matrix[3][4];

                        matrix_pixel[20] = Pixel_list.get(pixel_40)[k] * Gaussian_matrix[4][0];
                        matrix_pixel[21] = Pixel_list.get(pixel_41)[k] * Gaussian_matrix[4][1];
                        matrix_pixel[22] = Pixel_list.get(pixel_42)[k] * Gaussian_matrix[4][2];
                        matrix_pixel[23] = Pixel_list.get(pixel_43)[k] * Gaussian_matrix[4][3];
                        matrix_pixel[24] = Pixel_list.get(pixel_44)[k] * Gaussian_matrix[4][4];
                        for (int j = 0; j < 25; j++)
                            sum[k - 2] += matrix_pixel[j];
                    }
                    avg_R = sum[0] / 273;
                    avg_G = sum[1] / 273;
                    avg_B = sum[2] / 273;
                }
                int[] temp_pixel = {Pixel_list.get(i)[0], Pixel_list.get(i)[1], avg_R, avg_G, avg_B};
                Pixel_blur_list.add(temp_pixel);
            }
            Log.d(TAG, "Blur is finished");
        }
        else
            Log.d(TAG, "need get Bitmap");
    }
    //3. Equalizing  the Pixel_gray_list {Pixel_gray_list need} **Color Equalizing
    public void Bitmap_Histo_Equalization(){
        Log.d(TAG,"Histogram Equalization Start");
        int size = Pixel_gray_list.size();
        int[] Hist = new int[256];
        int max = 0;
        for(int i=0; i<size; i++) {
            Hist[Pixel_gray_list.get(i)[2]]++;
            if(max< Pixel_gray_list.get(i)[2])
                max= Pixel_gray_list.get(i)[2];
        }
        //Log.d(TAG,"Size : " + size + " Max R : " + max_R + " G :"+max_G + " B :"+ max_B);
        float[] equalization = new float[256];
        float accumulate_sum = 0;
        for(int k=0; k<256; k++){
            accumulate_sum += Hist[k]; //Built the grpah
            equalization[k] = (accumulate_sum * max /size);
            //Explanation :
            //Making First order linear formula by accumulating the values.
            //if y=ax ->  a = max/size (y intercept is zero cus the min value is 0)
            // now the height of bar is x
            // so the changed height of Histogram y= a* height of histogram.
            //Or in interpreting in probability
            //The percentage of that specific pixels exists at a certain area times the normalize value.
        }
        for(int i=0; i<size; i++) {
            Pixel_gray_list.get(i)[2] = (int) equalization[Pixel_gray_list.get(i)[2]];
        }
        Log.d(TAG,"Histogram Equalization Finish");
    }
    //4. Set the Pixel_gray_list {Pixel_blur_list need} ** Brightness Scaling
    public void Bitmap_Gray_scaling() {
        Log.d(TAG, "Gray scale working");
        if (Pixel_blur_list != null) {
            Pixel_gray_list = new ArrayList<>();
            int size = Pixel_blur_list.size();
            for (int i = 0; i < size; i++) {
                int x = Pixel_blur_list.get(i)[0];
                int y = Pixel_blur_list.get(i)[1];
                int R = Pixel_blur_list.get(i)[2];
                int G = Pixel_blur_list.get(i)[3];
                int B = Pixel_blur_list.get(i)[4];
                double Y_R = Math.pow(R*0.299,2);
                double Y_G = Math.pow(G*0.7152,2);
                double Y_B = Math.pow(B*0.0722,2);
                int Gray = (int)Math.sqrt( Y_R + Y_G + Y_B);
                int[] temp_pixel = {x,y,Gray,Gray,Gray};
                Pixel_gray_list.add(temp_pixel);
                }
            }
        Log.d(TAG, "Gray scale finished");
    }
    //5. Set the Pixel_mask_list {Pixel_gray_list need}
    public void Bitmap_Change_Mask() {
        Log.d(TAG, "Masking working");
        if (Mask_x != null && Mask_y != null && Pixel_gray_list != null) {
            int Sum_Mask_x = 0;
            int Sum_Mask_y = 0;
            int size = Pixel_gray_list.size();
            int last_x = Pixel_gray_list.get(size-1)[0];
            int last_y = Pixel_gray_list.get(size-1)[1];
            int w = last_x + 1;
            Pixel_mask_list = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                Sum_Mask_x = 0;
                Sum_Mask_y = 0;
                if (Pixel_gray_list.get(i)[0] != 0 && Pixel_gray_list.get(i)[1] != 0 && Pixel_gray_list.get(i)[0] != last_x && Pixel_gray_list.get(i)[1] != last_y) {
                    /*
                    00 |         01        | 02
                    10 |     current Pixel | 12
                    20 |         21        | 22
                    */
                    int pixel_00 = i - w - 1;
                    int pixel_01 = i - w;
                    int pixel_02 = i - w + 1;
                    int pixel_10 = i - 1;
                    int pixel_12 = i + 1;
                    int pixel_20 = i + w - 1;
                    int pixel_21 = i + w;
                    int pixel_22 = i + w + 1;

                    int value_00 = Pixel_gray_list.get(pixel_00)[2];
                    int value_01 = Pixel_gray_list.get(pixel_01)[2];
                    int value_02 = Pixel_gray_list.get(pixel_02)[2];
                    int value_10 = Pixel_gray_list.get(pixel_10)[2];
                    int value_11 = Pixel_gray_list.get(i)[2];
                    int value_12 = Pixel_gray_list.get(pixel_12)[2];
                    int value_20 = Pixel_gray_list.get(pixel_20)[2];
                    int value_21 = Pixel_gray_list.get(pixel_21)[2];
                    int value_22 = Pixel_gray_list.get(pixel_22)[2];

                    Sum_Mask_x += Mask_x[0][0]*value_00 + Mask_x[0][1]*value_01 + Mask_x[0][2]*value_02
                            + Mask_x[1][0]*value_10 + Mask_x[1][1]*value_11 + Mask_x[1][2]*value_12
                            + Mask_x[2][0]*value_20 + Mask_x[2][1]*value_21 + Mask_x[2][2]*value_22 ;
                    Sum_Mask_y += Mask_y[0][0]*value_00 + Mask_y[0][1]*value_01 + Mask_y[0][2]*value_02
                            + Mask_y[1][0]*value_10 + Mask_y[1][1]*value_11 + Mask_y[1][2]*value_12
                            + Mask_y[2][0]*value_20 + Mask_y[2][1]*value_21 + Mask_y[2][2]*value_22 ;
                }
                int G_x = Sum_Mask_x;
                int G_y = Sum_Mask_y;
                // (Sum_Mask_x > 0 ? Sum_Mask_x : -Sum_Mask_x) + (Sum_Mask_y > 0 ? Sum_Mask_y : -Sum_Mask_y);
                int G_value = (int) Math.sqrt(Math.pow(G_x,2) + Math.pow(G_y,2));
                if(G_value > 255)
                    G_value = 255;
                int[] temp_pixel = {Pixel_gray_list.get(i)[0],Pixel_gray_list.get(i)[1], G_value, G_x, G_y};
                Pixel_mask_list.add(temp_pixel);
            }
            Log.d(TAG, "Masking is finished ");
        }else
            Log.d(TAG, "Mask set or Gray Scale List is empty ");
     }
    //6 Set the Thresholding Value {Global value Num_Strong, Num_Weak, Pixel_mask_list need}
    public void OTsu(){
        Log.d(TAG,"Otsu start");
        int size = Pixel_mask_list.size();
        int[] Hist = new int[256];
        int smallest = 99999999;
        int max_t = 0;
        int medium_t = 0;
        float max_v = 0;
        //I don't think the end and first point will be threshold value.
        for (int i = 0; i < size; i++) {
            //THe G_value should be scaled in 0~ 255
            int scaling = Pixel_mask_list.get(i)[2];
            Hist[scaling]+= 1;
        }
        for(int T=1; T<255; T++) {
            for(int T_m=1; T_m<T; T_m++) {
                //Part 1
                float p_1 = 0;
                float mean_1 = 0;
                float v_1 = 0;
                for (int i = 0; i < T_m; i++) {
                    p_1 += Hist[i];
                    mean_1 += i * Hist[i];
                }
                mean_1 = mean_1 / p_1;
                for (int i = 0; i < T_m; i++) {
                    v_1 += Math.pow(i - mean_1, 2) * Hist[i];
                }
                v_1 = v_1 / p_1;
                p_1 = p_1 / size;
                //Part 2
                float p_2 = 0;
                float mean_2 = 0;
                float v_2 = 0;
                for (int i = T_m; i < T; i++) {
                    p_2 += Hist[i];
                    mean_2 += i * Hist[i];
                }
                mean_2 = mean_2 / p_2;
                for (int i = T_m; i < T; i++) {
                    v_2 += Math.pow(i - mean_2, 2) * Hist[i];
                }
                v_2 = v_2 / p_2;
                p_2 = p_2 / size;
                //Part 3
                float p_3 = 0;
                float mean_3 = 0;
                float v_3 = 0;
                for (int i = T; i < 256; i++) {
                    p_3 += Hist[i];
                    mean_3 += i * Hist[i];
                }
                mean_3 = mean_3 / p_3;
                for (int i = T; i < 256; i++) {
                    v_2 += Math.pow(i - mean_3, 2) * Hist[i];
                }
                v_3 = v_3 / p_3;
                p_3 = p_3 / size;
                //Final Variance
                float v_t = p_1 * v_1 + p_2 * v_2 + p_3 * v_3;
                if (smallest > v_t) {
                    medium_t = T_m;
                    max_t = T;
                    smallest = (int) v_t;
                }
            }
        }
        Num_Strong = max_t;
        Num_Weak = medium_t;
        Log.d(TAG,"Threshold  <Strong = " + Num_Strong + "> <Weak =" + Num_Weak + ">");
        Log.d(TAG,"Otsu end");
    }
     //7, Set the Pixel_Non_list {Pixel_mask_list need}
    public void Non_maximum_suppression() {
        Log.d(TAG, "Non maximum suppression is working");
        if (Pixel_mask_list != null) {

            Pixel_Non_list = new ArrayList<>();
            int size = Pixel_mask_list.size();
            int last_x = Pixel_mask_list.get(size - 1)[0];
            int last_y = Pixel_mask_list.get(size - 1)[1];
            int w = last_x + 1;
            for (int i = 0; i < size; i++) {
                int x = Pixel_mask_list.get(i)[0];
                int y = Pixel_mask_list.get(i)[1];
                int c = Pixel_mask_list.get(i)[2];
                int gx = Pixel_mask_list.get(i)[3];
                int gy = Pixel_mask_list.get(i)[4];
                if (Pixel_mask_list.get(i)[0] != 0 && Pixel_mask_list.get(i)[1] != 0 && Pixel_mask_list.get(i)[0] != last_x && Pixel_mask_list.get(i)[1] != last_y) {
                      /* cue the Mask set is double dimension array
                    00 |         01        | 02
                    10 |     current Pixel | 12
                    20 |         21        | 22
                    */
                    int  Gx = Pixel_mask_list.get(i)[3];
                    int  Gy = Pixel_mask_list.get(i)[4];
                    int Angle = (int) (Math.atan2(Gy, Gx)/3.14 * 180);
                    //              0 1 2  3 4 5  6 7 8
                    int[] Matrix = {0,0,0, 0,1,0, 0,0,0};
                    //Log.d(TAG,"Angle : "+ Angle);
                    ArrayList<Integer> new_list = new ArrayList<>();
                    new_list.add(Pixel_mask_list.get(i)[2]);
                    if((-22.5<= Angle && Angle <22.5) || (157.5<= Angle && Angle <202.5) ){
                        new_list.add(Pixel_mask_list.get(i - 1)[2]);
                        new_list.add(Pixel_mask_list.get(i + 1)[2]);
                    }else if((22.5<= Angle && Angle <67.5) || (202.5<= Angle && Angle <247.5) ){
                        new_list.add(Pixel_mask_list.get(i - w + 1)[2]);
                        new_list.add(Pixel_mask_list.get(i + w - 1)[2]);
                    }else if((112.5<= Angle && Angle <157.5) || (247.5<= Angle && Angle <292.5) ){
                        new_list.add(Pixel_mask_list.get(i - w)[2]);
                        new_list.add(Pixel_mask_list.get(i + w)[2]);
                    }else if((67.5<= Angle && Angle <112.5) || (292.5<= Angle && Angle <315) ){
                        new_list.add(Pixel_mask_list.get(i - w - 1)[2]);
                        new_list.add(Pixel_mask_list.get(i + w + 1)[2]);
                    }else if(-157.5<= Angle && Angle <-202.5){
                        new_list.add(Pixel_mask_list.get(i - 1)[2]);
                        new_list.add(Pixel_mask_list.get(i + 1)[2]);
                    }else if((-22.5<= Angle && Angle <6-7.5) || (-202.5<= Angle && Angle <-247.5) ){
                        new_list.add(Pixel_mask_list.get(i - w - 1)[2]);
                        new_list.add(Pixel_mask_list.get(i + w + 1)[2]);
                    }else if((-112.5<= Angle && Angle <-157.5) || (-247.5<= Angle && Angle <-292.5) ){
                        new_list.add(Pixel_mask_list.get(i - w)[2]);
                        new_list.add(Pixel_mask_list.get(i + w)[2]);
                    }else if((-67.5<= Angle && Angle <-112.5) || (-292.5<= Angle && Angle <-315) ){
                        new_list.add(Pixel_mask_list.get(i - w + 1)[2]);
                        new_list.add(Pixel_mask_list.get(i + w - 1)[2]);
                    }
                    int  max  = Collections.max(new_list);
                    if(max != Pixel_mask_list.get(i)[2])
                        c = 0;
                    /*

                    if (!max){
                        c = 0;}
                    int pixel_00 = i - w - 1;
                    int pixel_01 = i - w;
                    int pixel_02 = i - w + 1;
                    int pixel_10 = i - 1;
                    int pixel_12 = i + 1;
                    int pixel_20 = i + w - 1;
                    int pixel_21 = i + w;
                    int pixel_22 = i + w + 1;

                    int[] find_max_list = new int[9];
                    find_max_list[0] = Pixel_mask_list.get(pixel_00)[2];
                    find_max_list[1] = Pixel_mask_list.get(pixel_01)[2];
                    find_max_list[2] = Pixel_mask_list.get(pixel_02)[2];
                    find_max_list[3] = Pixel_mask_list.get(pixel_10)[2];
                    find_max_list[4] = Pixel_mask_list.get(i)[2];
                    find_max_list[5] = Pixel_mask_list.get(pixel_12)[2];
                    find_max_list[6] = Pixel_mask_list.get(pixel_20)[2];
                    find_max_list[7] = Pixel_mask_list.get(pixel_21)[2];
                    find_max_list[8] = Pixel_mask_list.get(pixel_22)[2];
                    int k=0;
                    boolean max = true;
                    while(k < 9) {
                        if (find_max_list[4] < find_max_list[k]) {
                            max = false;
                            break;
                        }
                        k++;
                    }
                    */
                }
                int[] temp_pixel = new int[]{x, y, c, gx, gy};
                Pixel_Non_list.add(temp_pixel);
            }
            Log.d(TAG, "Non maximum suppression is finished");
        }else
            Log.d(TAG, "Pixel mask List is empty ");
    }
    //8. Set the Pixel_Strong_list {Pixel_Non_list need & Num_Strong & Num_Weak need}
    public void Connecting_algorithm() {
        Log.d(TAG,"Connecting Start");
        Pixel_Connect_list = Pixel_Non_list;
        int size = Pixel_Connect_list.size();
        ArrayList<Integer> weak_num = new ArrayList<>();
        //We are going to analyze only the weak pixel
        for(int i =0; i<size; i++) {
            //this is Threshold method
            if(Pixel_Connect_list.get(i)[2] > Num_Strong)
                Pixel_Connect_list.get(i)[2] = 255;
            else if (Pixel_Connect_list.get(i)[2] < Num_Strong && Pixel_Connect_list.get(i)[2] > Num_Weak)
                weak_num.add(i);
            else
                Pixel_Connect_list.get(i)[2] = 0;
        }
        int num = 0;//Current index from Pixel_Non_list. It is start from the 0
        while (weak_num.size()>0) {
            //Log.d(TAG,"Left" + weak_num.size());
            int last_x = Pixel_Connect_list.get(size - 1)[0];
            int last_y = Pixel_Connect_list.get(size - 1)[1];
            int w = last_x + 1;
            int h = last_y + 1;
            ArrayList<Integer> Close_list = new ArrayList<>();
            ArrayList<Integer> Open_list = new ArrayList<>();
            //if the all connected pixel result shows ture it means one of connected pixel is strong pixel.
            if (connecting(Close_list, Open_list, w, h, num)) {
                Close_list.addAll(Open_list);
                for (int k = 0; k < Close_list.size(); k++) {
                    Pixel_Connect_list.get(Close_list.get(k))[2] = 255; //if it is connected with strong pixel it is 255
                    // if not it was already defined in connecting algorithm as 0 so we don't need to change the color value
                }
            }
            //It puts in all the concatenated values ​​you may have left pixels where in open_list.
            Open_list.clear();
            Close_list.clear();
            //if the pixel is already inspected, we take out the pixel from the weak pixel list
            num = weak_num.get(0);
            weak_num.remove(0);
        }
        Log.d(TAG,"Connecting Finish");
    }
    //Recursive function-> Why we use this? to find the connected the pixels.
    private boolean connecting(ArrayList<Integer> Close_list, ArrayList<Integer> Open_list, int w, int h, int num){
        Close_list.add(num);
        int x = num/w;
        int y = num%w;
        Pixel_Connect_list.get(num)[2] = -1;
        //If it is not the border of the image. Why we check it is not located on the border of the image? cus find the 8 direction nearby pixel from it.
        if(x >0 && x<(w-1) && y >0 && y < (h-1)) {
            //if it is strong pixel
            if (Pixel_Non_list.get(num)[2] > Num_Strong)
                return true;
            //if it is a weak pixel
            else {
                //find the nearby coordinates of pixel by index.
                int pixel_00 = num - w - 1;
                int pixel_01 = num - w;
                int pixel_02 = num - w + 1;
                int pixel_10 = num - 1;
                int pixel_12 = num + 1;
                int pixel_20 = num + w - 1;
                int pixel_21 = num + w;
                int pixel_22 = num + w + 1;
                //Put Strong or Weak pixel from 8directions-> Why we put both pixels instead of the only weak pixels?
                // Because the inspecting for strong or weak is the beginning of this function. So we don't need more boring steps to distinguish each type.
                if (Pixel_Connect_list.get(pixel_00)[2] > Num_Weak ) {
                    Open_list.add(pixel_00);
                    Pixel_Connect_list.get(pixel_00)[2] = 0;
                    //Just the changed the value of Pixel as zero so it is not included in the open_list without checking it is already in close list.
                    //More over, we don't need to drawing it again because if these connected pixel show that these are not connected with the strong pixel, it just becomes zero color
                }
                if (Pixel_Connect_list.get(pixel_01)[2] > Num_Weak ) {
                    Open_list.add(pixel_01);
                    Pixel_Connect_list.get(pixel_01)[2] = 0;
                }
                if (Pixel_Connect_list.get(pixel_02)[2] > Num_Weak ) {
                    Open_list.add(pixel_02);
                    Pixel_Connect_list.get(pixel_02)[2] = 0;
                }
                if (Pixel_Connect_list.get(pixel_10)[2] > Num_Weak ) {
                    Open_list.add(pixel_10);
                    Pixel_Connect_list.get(pixel_10)[2] = 0;
                }
                if (Pixel_Connect_list.get(pixel_12)[2] > Num_Weak ) {
                    Open_list.add(pixel_12);
                    Pixel_Connect_list.get(pixel_12)[2] = 0;
                }
                if (Pixel_Connect_list.get(pixel_20)[2] > Num_Weak ) {
                    Open_list.add(pixel_20);
                    Pixel_Connect_list.get(pixel_20)[2] = 0;
                }
                if (Pixel_Connect_list.get(pixel_21)[2] > Num_Weak ) {
                    Open_list.add(pixel_21);
                    Pixel_Connect_list.get(pixel_21)[2] = 0;
                }
                if (Pixel_Connect_list.get(pixel_22)[2] > Num_Weak ) {
                    Open_list.add(pixel_22);
                    Pixel_Connect_list.get(pixel_22)[2] = 0;
                }
                //take out from One factor from Open_list-> Open_list includes the near by pixels of each pixel.
                while (Open_list.size() > 0) {
                    int target = Open_list.get(0); //index is changed to the target
                    Open_list.remove(0); //taking out it from the open list
                    //if 'it is true' means that one of child connected pixel is strong pixel -> so all of this connected pixels become strong pixel
                    if (connecting(Close_list, Open_list, w, h, target))
                        return true;
                }
                //Even though searching all of pixel list there is no more pixel, it moving to the parent pixel
                return false;
            }
        }
        //If this pixel is located on border of image return false
        return false;
    }
