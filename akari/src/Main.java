import java.io.*;

public class Main {
    public static void main(String[] args){
        int A=0,k=0,a=0,r=0,i=0;
        int n = args.length;
        String s = "P%d\n%d\40%d\n%d\n\00wb+";
        char[] d=s.toCharArray();
        byte[] b = new byte[1024];
        s = "yuriyurarararayuruyuri*daijiken**akkari~n**/y*u*k/riin<ty(uyr)g,aur,arr[a1r2a82*y2*/u*r{uyu}riOcyurhiyua**rrar+*arayra*=yuruyurwiyuriyurara'rariayuruyuriyuriyu>rarararayuruy9uriyu3riyurar_aBrMaPrOaWy^?*]/f]`;hvroai<dp/f*i*s/<ii(f)a{tpguat<cahfaurh(+uf)a;f}vivn+tf/g*`*w/jmaa+i`ni(i+k[>+b+i>++b++>l[rb\0";
        char[] y=s.toCharArray();
        int u;
        for (i = 0; i < 101; i++)
        {
            s = "~hktrvg~dmG*eoa+%squ#l2:(wn\"1l))v?wM353{/Y;lgcGp`vedllwudvOK`cct~[|ju {stkjalor(stwvne\"gt\"yogYURUYURI";
            char tmp = s.charAt(i);
            y[i*2]^= (tmp^y[i*2+1]^4);
        }
        OutputStream q;
        InputStream p;
        try {
            if (n > 1 && (args[1].charAt(0) != '-' || args[1].length() > 1)) {
                p = new FileInputStream(args[1]);
            }
            else {
                p = System.in;
            }
        }
        catch (Exception e) {
            System.out.printf("Can not open\40%s\40for\40%sing\n", args[1], "read");
            return;
        }
        try {
            if (n < 3 || !(args[2].charAt(0) != '-' || args[2].length() > 1)) {
                q = System.out;
            }
            else q = new FileOutputStream(args[2]);
        }
        catch (Exception e) {
            System.out.printf("Can not open\40%s\40for\40%sing\n", args[2], "writ");
            return;
        }
        for (a=k=u=0; y[u] != '\0'; u+=2)
        {
            y[k++] = y[u];
        }
        try {
            a = p.read(b, 0, 1024);
        }
        catch (Exception e) {
            System.out.println("Exception while reading");
            return;
        }
        boolean is_good = true;
        try {
            s = new String(b).replace(' ', '\n');
            if (b[0] != 'P') is_good = false;
            String[] substr = s.replace('P', '\n').split("\n");
            k = Integer.parseInt(substr[1]);
            A = Integer.parseInt(substr[2]);
            i = Integer.parseInt(substr[3]);
            r = Integer.parseInt(substr[4]);
        }
        catch (Exception e) {
            is_good = false;
        }
        //System.out.printf("%d %d %d %d", k, A, i, r);
        if(a > 2 && is_good && !(k-6 != 0 && k-5 != 0) && r == 255)
        {
            u=A;
            if (n > 3)
            {
                u++;
                i++;
            }
            s = String.format("P%d\n%d\40%d\n%d\n", k, u >> 1, i >> 1, r);
            try {
                q.write(s.getBytes());
            }
            catch (Exception e) {
                System.out.println("Exception while writing");
            }
            if (k - 5 != 0) u = 8;
            else u = 4;
            k = 3;
        }
        else
        {
            if (n > 3) u = 2;
            else u = 10;
        }
        for (r=i=0;;)
        {
            u *= 6;
            if (n > 3) u += 1;
            if ((y[u] & 1) != 0) {
                try {
                    q.write(r);
                }
                catch (Exception e) {
                    System.out.println("Exception while writing");
                }
            }
            if ((y[u] & 16) != 0) k = A;
            if ((y[u] & 2) != 0) k--;
            if (i == a)
            {
                i = a = (u*11);
                try {
                    a = p.read(b, 0, 1024);
                }
                catch (Exception e) {
                    System.out.println("Exception while reading");
                }
                if (0 >= a) break;
                i=0;
            }
            r = b[i++];
            int tmp1;
            if ((y[u] & 4) != 0 && k != 0) {
                tmp1 = 2;
            }
            else if ((y[u] & 4) != 0) {
                tmp1 = 4;
            }
            else {
                tmp1 = 2;
            }
            int tmp2;
            if (10 - r != 0) {
                tmp2 = 4;
            }
            else tmp2 = 2;
            if ((8 & y[u]) != 0) {
                u += tmp2;
            }
            else u += tmp1;
            u = y[u] - 96;
        }
        try {
            p.close();
        }
        catch (Exception e) {
            System.out.println("Exception while closing input file");
        }
        try {
            q.close();
        }
        catch (Exception e) {
            System.out.println("Exception while closing output file");
        }
        return;
    }
}
