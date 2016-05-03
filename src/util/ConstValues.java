package util;

/**
 * Created by Jason on 2016/5/3.
 */
public class ConstValues {
    public static final int MAGIC_VALUE = 0xCAFEBABE;

    public static final class ConstantPool {
        public static final int CONSTANT_Utf8_info = 1;
        public static final int Constant_Integer_info = 3;
        public static final int Constant_Float_info = 4;
        public static final int Constant_Long_info = 5;
        public static final int Constant_Double_info = 6;
        public static final int Constant_Class_info = 7;
        public static final int Constant_String_info = 8;
        public static final int Constant_Fieldref_info = 9;
        public static final int Constant_Methodref_info = 10;
        public static final int Constant_InterfaceMethodref_info = 11;
        public static final int Constant_NameAndType_info = 12;
        public static final int Constant_MethodHandle_info = 15;
        public static final int Constant_MethodType_info = 16;
        public static final int Constant_InvokeDynamic_info = 18;
    }

    public static final class ConstantPoolType {
        public static final String Class = "Class";
        public static final String Fieldref = "Fieldref";
        public static final String Methodref = "Methodref";
        public static final String Utf8 = "Utf8";
        public static final String NameAndType = "NameAndType";
    }

}
