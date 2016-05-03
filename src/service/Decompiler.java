package service;

import util.ConstValues;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created by Jason on 2016/5/3.
 */
public class Decompiler {

    private DataInputStream dataInputStream;
    private String[] pool_specific_values;

    public Decompiler(DataInputStream dataInputStream) {
        this.dataInputStream = dataInputStream;
    }

    public void analyse() {
        try {
            int magic = readMagic();
            System.out.println("magic code is: 0x" + Integer.toHexString(magic).toUpperCase());
            double version = readVersion();
            System.out.println("version is: "+version);
            readConstantContents();
            String accessFlag = readAccessFlag();
            System.out.println("accessFlag is: "+accessFlag);
            String thisClass = readThisClass();
            System.out.println("this class is: "+thisClass);
            String superClass = readSuperClass();
            System.out.println("super class is: "+superClass);
            String interfaces = readInterfaces();
            System.out.println("interfaces are: "+interfaces);
            readFieldsInfo();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int readMagic() throws IOException {
        int magic = u4(dataInputStream);
        return magic;
    }

    public double readVersion() throws IOException {
        short minorVersion = u2(dataInputStream);
        short majorVersion = u2(dataInputStream);
        if (minorVersion==0x0003 && majorVersion == 0x002D) {
            return 45.3;
        } else if (minorVersion==0x0000) {
            double version = minorVersion << 16 | majorVersion;
            return version;
        } else {
            return -1;
        }
    }

    public void readConstantContents() throws IOException {
        int count = readConstantPoolsCount();
        System.out.println("constant pools count is: "+count);
        String[] pool_types = new String[count];
        String[] pool_values = new String[count];
        pool_specific_values = new String[count];
        for (int i=1;i<count;i++) {
            String[] result = readConstantContent();
            pool_types[i] = result[0];
            pool_values[i] = result[1];
        }
        for (int i=1;i<count;i++) {
            pool_specific_values[i] = getSpecificValue(pool_types,pool_values,i,count);
        }
        for (int i=1;i<count;i++) {
            System.out.printf("%10s","#" + i + " = ");
            System.out.printf("%-20s", pool_types[i]);
            System.out.printf("%-30s",pool_values[i]);
            if (!pool_values[i].equals(pool_specific_values[i])) {
                System.out.printf("%-30s", "//\t"+pool_specific_values[i]);
            }
            System.out.println();
        }
    }

    /**
     * fina the root value in the constant pools based on the index
     * @param pool_types types of constant pool
     * @param pool_values values of constant pool
     * @param idx current index
     * @param count size of constant pools
     * @return
     */
    public String getSpecificValue(String[] pool_types,String[] pool_values,int idx, int count) {
        if (idx<0||idx>=count) {
            System.out.println("invalid constant pool index");
            System.exit(0);
            return "";
        } else if (pool_types[idx].equals(ConstValues.ConstantPoolType.Class)) {
            int index = Integer.valueOf(pool_values[idx].substring(1));
            return getSpecificValue(pool_types,pool_values,index,count);
        } else if (pool_types[idx].equals(ConstValues.ConstantPoolType.Methodref)) {
            String[] values = pool_values[idx].split("\\.");
            int index1 = Integer.valueOf(values[0].substring(1));
            int index2 = Integer.valueOf(values[1].substring(1));
            return getSpecificValue(pool_types,pool_values,index1,count) + "." +
                    getSpecificValue(pool_types,pool_values,index2,count);
        } else if (pool_types[idx].equals(ConstValues.ConstantPoolType.NameAndType)) {
            String[] values = pool_values[idx].split(":");
            int index1 = Integer.valueOf(values[0].substring(1));
            int index2 = Integer.valueOf(values[1].substring(1));
            return getSpecificValue(pool_types,pool_values,index1,count) + ":" +
                    getSpecificValue(pool_types,pool_values,index2,count);
        } else {
            return pool_values[idx];
        }

    }

    public String[] readConstantContent() throws IOException {
        byte tag = u1(dataInputStream);
        String[] result = new String[2];
        String type = "", value = "";
        switch (tag) {
            case ConstValues.ConstantPool.CONSTANT_Utf8_info:   //length bytes
                int length = u2(dataInputStream);
                byte[] data = new byte[length];
                dataInputStream.read(data);
                type = ConstValues.ConstantPoolType.Utf8;
                value = new String(data);
                break;
            case ConstValues.ConstantPool.Constant_Integer_info: //4 bytes
                int int_value = u4(dataInputStream);
                type = ConstValues.ConstantPoolType.Utf8;
                value = int_value+"";
                break;
            case ConstValues.ConstantPool.Constant_Float_info:  //4 bytes
                float float_value = u4(dataInputStream);
                type = ConstValues.ConstantPoolType.Utf8;
                value = float_value + "";
                break;
            case ConstValues.ConstantPool.Constant_Long_info:   //8 bytes
                long long_value = u8(dataInputStream);
                type = ConstValues.ConstantPoolType.Utf8;
                value = long_value + "";
                break;
            case ConstValues.ConstantPool.Constant_Double_info: //8 bytes
                double double_value = u8(dataInputStream);
                type = ConstValues.ConstantPoolType.Utf8;
                value = double_value + "";
                break;
            case ConstValues.ConstantPool.Constant_Class_info:
                short class_index = u2(dataInputStream);
                type = ConstValues.ConstantPoolType.Class;
                value = "#"+class_index;
                break;
            case ConstValues.ConstantPool.Constant_String_info: //index
                short string_index = u2(dataInputStream);
                type = ConstValues.ConstantPoolType.Utf8;
                value = "#"+string_index;
                break;
            case ConstValues.ConstantPool.Constant_Fieldref_info:
                short filed_class_index = u2(dataInputStream);  //constant_class_info index
                short field_nameAndType_index = u2(dataInputStream);    //NameAndType index
                type = ConstValues.ConstantPoolType.Fieldref;
                value = "#"+filed_class_index+"."+"#"+field_nameAndType_index;
                break;
            case ConstValues.ConstantPool.Constant_Methodref_info:
                short method_class_index = u2(dataInputStream);  //constant_class_info index
                short method_nameAndType_index = u2(dataInputStream);    //NameAndType index
                type = ConstValues.ConstantPoolType.Methodref;
                value = "#"+method_class_index+"."+"#"+method_nameAndType_index;
                break;
            case ConstValues.ConstantPool.Constant_InterfaceMethodref_info:
                short interface_method_class_index = u2(dataInputStream);  //constant_class_info index
                short interface_method_nameAndType_index = u2(dataInputStream);    //NameAndType index
                type = "InterfaceMethodref";
                value = "#"+interface_method_class_index+":"+"#"+interface_method_nameAndType_index;
                break;
            case ConstValues.ConstantPool.Constant_NameAndType_info:
                short name_index = u2(dataInputStream);   //name index
                short type_index = u2(dataInputStream);   //type index
                type = ConstValues.ConstantPoolType.NameAndType;
                value = "#"+name_index+":"+"#"+type_index;
                break;
            case ConstValues.ConstantPool.Constant_MethodHandle_info:
                byte reference_kind = u1(dataInputStream); //between 1 and 9
                short reference_index = u2(dataInputStream);  //index to constant pools
                break;
            case ConstValues.ConstantPool.Constant_MethodType_info:
                short description_index = u2(dataInputStream);  //index to constant pools
                break;
            case ConstValues.ConstantPool.Constant_InvokeDynamic_info:
                short bootstrap_method_attr_index = u2(dataInputStream);  //index to bootstrap_methods[]
                short name_and_type_index = u2(dataInputStream);  //index to constant pools
                break;
        }
        result[0] = type;
        result[1] = value;
        return result;
    }

    public short readConstantPoolsCount() throws IOException {
        short count = u2(dataInputStream);
        return count;
    }

    public String readAccessFlag() throws IOException {
        short accessFlag = u2(dataInputStream);
        int size = ConstValues.AccessFlag.AccessFlags.length;
        StringBuilder builder = new StringBuilder();
        for (int i=0;i<size;i++) {
            if ((accessFlag&ConstValues.AccessFlag.AccessFlags[i])!=0) {
                builder.append(ConstValues.AccessFlag.AccessName[i]);
                if (i!=size-1) {
                    builder.append(" ");
                }
            }
        }
        return builder.toString();
    }

    public String readThisClass() throws IOException {
        short index = u2(dataInputStream);
        return pool_specific_values[index];
    }

    public String readSuperClass() throws IOException {
        short index = u2(dataInputStream);
        return pool_specific_values[index];
    }

    public String readInterfaces() throws IOException {
        short count = u2(dataInputStream);
        System.out.println("interface count is: "+count);
        StringBuilder builder = new StringBuilder();
        for (short i=0;i<count;i++) {
            int index = u2(dataInputStream);
            builder.append(pool_specific_values[index]);
            if (i!=count-1) {
                builder.append(" ");
            }
        }
        return builder.toString();
    }

    public void readFieldsInfo() throws IOException {
        short field_count = u2(dataInputStream);
        for (int i=0;i<field_count;i++) {
            readFieldInfo();
        }
    }

    public void readFieldInfo() throws IOException {
        String accessFlag = readFieldAccessFlag();
        short name_index = u2(dataInputStream);
        String name = pool_specific_values[name_index];
        short descriptor_index = u2(dataInputStream);
        String descriptor = pool_specific_values[descriptor_index];
        short attribute_count = u2(dataInputStream);
        System.out.println(accessFlag+" "+name+" "+descriptor);
    }

    private String readFieldAccessFlag() throws IOException {
        short accessFlag = u2(dataInputStream);
        StringBuilder builder = new StringBuilder();
        int size = ConstValues.FieldAccessFlag.AccessFlags.length;
        for (int i=0;i<size;i++) {
            if ((accessFlag&ConstValues.FieldAccessFlag.AccessFlags[i])!=0) {
                builder.append(ConstValues.FieldAccessFlag.AccessName[i]);
                if (i!=size-1) {
                    builder.append(" ");
                }
            }
        }
        return builder.toString();
    }

    public static byte u1(DataInputStream dataInputStream) throws IOException {
        return dataInputStream.readByte();
    }

    public static short u2(DataInputStream dataInputStream) throws IOException {
        return dataInputStream.readShort();
    }

    public static int u4(DataInputStream dataInputStream) throws IOException {
        return dataInputStream.readInt();
    }

    public static long u8(DataInputStream dataInputStream) throws IOException {
        return dataInputStream.readLong();
    }

}
