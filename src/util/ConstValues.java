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

    public static final class AccessFlag {
        private static final int ACC_PUBLIC = 0x0001;
        private static final int ACC_FINAL = 0x0010;
        private static final int ACC_SUPER = 0x0020;
        private static final int ACC_INTERFACE = 0x0200;
        private static final int ACC_ABSTRACT = 0x0400;
        private static final int ACC_SYNTHETIC = 0x1000;
        private static final int ACC_ANNOTATION = 0x2000;
        private static final int ACC_ENUM = 0x4000;

        public static final int[] AccessFlags = {ACC_PUBLIC,ACC_FINAL,ACC_SUPER,ACC_INTERFACE,
                ACC_ABSTRACT,ACC_SYNTHETIC,ACC_ANNOTATION,ACC_ENUM};

        public static final String[] AccessName = {"public","final","","interface","abstract",
                "","annotation","enum"};
    }

    public static final class FieldAccessFlag {
        private static final int ACC_PUBLIC = 0x0001;
        private static final int ACC_PRIVATE = 0x0002;
        private static final int ACC_PROTECTED = 0x0004;
        private static final int ACC_STATIC = 0x0008;
        private static final int ACC_FINAl = 0x0010;
        private static final int ACC_VOLATILE = 0x0040;
        private static final int ACC_TRANSIENT = 0x0080;
        private static final int ACC_SYNTHETIC = 0x1000;
        private static final int ACC_ENUM = 0x4000;

        public static final int[] AccessFlags = {ACC_PUBLIC,ACC_PRIVATE,ACC_PROTECTED,ACC_STATIC,
                ACC_FINAl,ACC_VOLATILE,ACC_TRANSIENT,ACC_SYNTHETIC,ACC_ENUM};

        public static final String[] AccessName = {"public","private","protected","static","final",
                "volatile","transient","","enum"};

    }

}
