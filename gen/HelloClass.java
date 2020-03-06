package gen;

import java.io.FileOutputStream;

import jdk.internal.org.objectweb.asm.tree.InsnList;
import jdk.internal.org.objectweb.asm.tree.MethodNode;
import jdk.internal.org.objectweb.asm.tree.VarInsnNode;
import jdk.nashorn.internal.ir.LabelNode;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;

import static org.objectweb.asm.Opcodes.*;

public class HelloClass {

    public static void main(String[] args) throws Exception {
//        weirdExampleWithCurrentTime();
        classicExample();
        generateSimpleClassObject();
        test();
    }

    private static void classicExample() throws Exception {
        ClassWriter cw = new ClassWriter(0);

        cw.visit(V1_6, ACC_PUBLIC + ACC_SUPER, "gen/HelloGen", null, "java/lang/Object", null);
        cw.visitField(ACC_PRIVATE, "value", "Ljava/lang/Integer;", null, null).visitEnd();

        //default constructor
        {
            MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
            mv.visitCode();
            mv.visitVarInsn(ALOAD, 0); //load the first local variable: this
            mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V");
            mv.visitInsn(RETURN);
            mv.visitMaxs(1, 1);
            mv.visitEnd();
        }

        //main method
        {
            MethodVisitor mv = cw.visitMethod(ACC_PUBLIC + ACC_STATIC, "main", "([Ljava/lang/String;)V", null, null);
            mv.visitCode();
            mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;"); //put System.out to operand stack
            mv.visitLdcInsn("Hello"); //load const "Hello" from const_pool, and put onto the operand stack
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V");
            mv.visitInsn(RETURN);
            mv.visitMaxs(2, 1);
            mv.visitEnd();
        }

        // method generation from documentation
        {
            MethodVisitor mv = cw.visitMethod(ACC_PUBLIC + ACC_STATIC, "addTwoIntegers", "(Ljava/lang/Long;Ljava/lang/Long;)java/lang/Long", null, null);
            mv.visitCode();
            mv.visitVarInsn(LLOAD, 0);
            mv.visitVarInsn(LLOAD, 1);
            mv.visitInsn(LADD);
            mv.visitVarInsn(LSTORE, 2); // сохраняем в local_variable_2
            mv.visitVarInsn(LLOAD, 2);

            mv.visitInsn(LRETURN);
            mv.visitMaxs(3, 3);
            mv.visitEnd();

        }

        // method subtract two integers
        {
            MethodVisitor mv = cw.visitMethod(ACC_PUBLIC + ACC_STATIC, "subtractTwoIntegers", "(Ljava/lang/Integer;Ljava/lang/Integer;)java/lang/Integer", null, null);
            mv.visitCode();
            mv.visitVarInsn(ILOAD, 0);
            mv.visitVarInsn(ILOAD, 1);
            mv.visitInsn(ISUB);
            mv.visitVarInsn(ISTORE, 2);
            mv.visitVarInsn(ILOAD, 2);
            mv.visitInsn(IRETURN);
            mv.visitMaxs(3, 3);
            mv.visitEnd();
        }

        // Add two reals
        {
            MethodVisitor mv = cw.visitMethod(ACC_PUBLIC + ACC_STATIC, "addTwoReals", "(Ljava/lang/Double;Ljava/lang/Double;)java/lang/Double", null, null);
            mv.visitCode();
            mv.visitVarInsn(DLOAD, 0);
            mv.visitVarInsn(DLOAD, 1);
            mv.visitInsn(DADD);
            mv.visitVarInsn(DSTORE, 2);
            mv.visitVarInsn(DLOAD, 2);
            mv.visitInsn(DRETURN);
            mv.visitMaxs(3, 3);
            mv.visitEnd();
        }

        // Add two reals
        {
            MethodVisitor mv = cw.visitMethod(ACC_PUBLIC + ACC_STATIC, "subtractTwoReals", "(Ljava/lang/Double;Ljava/lang/Double;)java/lang/Double", null, null);
            mv.visitCode();
            mv.visitVarInsn(DLOAD, 0);
            mv.visitVarInsn(DLOAD, 1);
            mv.visitInsn(DSUB);
            mv.visitVarInsn(DSTORE, 2);
            mv.visitVarInsn(DLOAD, 2);
            mv.visitInsn(DRETURN);
            mv.visitMaxs(3, 3);
            mv.visitEnd();
        }

        // Add two objects
        {
            MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "AddTwoObjects", "(Lgen/SimpleObject;Lgen/SimpleObject;)gen/SimpleObject", null, null);
            mv.visitCode();
            mv.visitVarInsn(ALOAD, 1);
            mv.visitFieldInsn(GETFIELD, "gen/SimpleObject", "value", "Ljava/lang/Integer;");
            mv.visitVarInsn(ISTORE, 3);

            mv.visitVarInsn(ALOAD, 2);
            mv.visitFieldInsn(GETFIELD, "gen/SimpleObject", "value", "Ljava/lang/Integer;");
            mv.visitVarInsn(ISTORE, 4);

            mv.visitVarInsn(ILOAD, 3);
            mv.visitVarInsn(ILOAD, 4);
            mv.visitInsn(IADD);
            mv.visitVarInsn(ISTORE, 5);

            mv.visitTypeInsn(NEW, "Lgen/SimpleObject;");
            mv.visitInsn(DUP);
            mv.visitVarInsn(ILOAD,5);

            mv.visitMethodInsn(INVOKESPECIAL, "gen/SimpleObject", "<init>", "(Ljava/lang/Integer;)V");

            mv.visitInsn(ARETURN);
            mv.visitEnd();
        }

        {
            MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "AddTwoObjects", "()V", null, null);
            mv.visitCode();
            mv.visitVarInsn(ALOAD,0);
            mv.visitTypeInsn(NEW,"java/lang/Object");
            mv.visitInsn(DUP);
            mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Object","<init>","()V");
        }

        {

        }

        cw.visitEnd();

        //save bytecode into disk
        FileOutputStream out = new FileOutputStream("/Users/ekaterina/IdeaProjects/ASMGradleTest/src/main/java/gen/HelloGen.class");
        out.write(cw.toByteArray());
        out.close();
    }

    private static void test() throws Exception{
        ClassWriter cw = new ClassWriter(0);
        MethodVisitor mv;

        cw.visit(V1_5, ACC_PUBLIC + ACC_SUPER, "generated", null, "java/lang/Object",
                new String[] { "gen/Test" });

        {
            mv = cw.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
            mv.visitCode();
            mv.visitVarInsn(ALOAD, 0);
            mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
            mv.visitInsn(RETURN);
            mv.visitMaxs(1, 1);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PUBLIC, "setMessage",
                    "(Lcom/github/ruediste/privateFieldCodegen/Sample;Ljava/lang/String;)V", null, null);
            mv.visitCode();
            mv.visitVarInsn(ALOAD, 1);
            mv.visitVarInsn(ALOAD, 2);
            mv.visitFieldInsn(PUTFIELD, "com/github/ruediste/privateFieldCodegen/Sample", "message",
                    "Ljava/lang/Integer;");
            mv.visitInsn(RETURN);
            mv.visitMaxs(2, 3);
            mv.visitEnd();
        }
        cw.visitEnd();

        //save bytecode into disk
        FileOutputStream out = new FileOutputStream("/Users/ekaterina/IdeaProjects/ASMGradleTest/src/main/java/gen/Test.class");
        out.write(cw.toByteArray());
        out.close();
    }

    private static void generateSimpleClassObject() throws Exception {
        ClassWriter cw = new ClassWriter(0);

        cw.visit(V1_6, ACC_PUBLIC + ACC_SUPER, "gen/SimpleObject", null, "java/lang/Object", null);
        cw.visitField(ACC_PUBLIC, "value", "Ljava/lang/Integer;", null, null).visitEnd();

        //default constructor
        {
            MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "<init>", "(Ljava/lang/Integer;)V", null, null);
            mv.visitCode();
            mv.visitVarInsn(ALOAD, 0);
            mv.visitVarInsn(ILOAD, 1);
            mv.visitFieldInsn(PUTFIELD, "gen/SimpleObject", "value", "Ljava/lang/Integer;");

            mv.visitInsn(RETURN);
            mv.visitMaxs(1, 1);
            mv.visitEnd();
        }

        cw.visitEnd();

        //save bytecode into disk
        FileOutputStream out = new FileOutputStream("/Users/ekaterina/IdeaProjects/ASMGradleTest/src/main/java/gen/SimpleObject.class");
        out.write(cw.toByteArray());
        out.close();
    }

    private static void weirdExampleWithCurrentTime() throws Exception {
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        cw.visit(V1_5, ACC_PUBLIC, "org/Test", null, "org/example/Example", null);
        cw.visitField(ACC_PRIVATE, "name", "Ljava/lang/String;", null, null).visitEnd();

        MethodVisitor methodVisitor = cw.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
        methodVisitor.visitCode();
        methodVisitor.visitVarInsn(ALOAD, 0);
        methodVisitor.visitMethodInsn(INVOKESPECIAL, "org/example/Example", "<init>", "()V");
        methodVisitor.visitInsn(RETURN);
        methodVisitor.visitMaxs(1, 1);
        methodVisitor.visitEnd();

        // Method run
        /**
         * Генерим метод
         * Идем внутрь
         * Засовываем currentTimeMillis в operandStack
         * Сохраняем _что-то_ в local_variable 2
         * Грузим ссылку из local_variable 0 в operand_stack
         * Грузим сслыку из local_variable 1 в operand_stack
         * Засовываем метод run в operand_stack
         *
         */
        methodVisitor = cw.visitMethod(ACC_PUBLIC, "run", "(Ljava/lang/String;)V", null, null);
        methodVisitor.visitCode();
        methodVisitor.visitMethodInsn(INVOKESTATIC, "java/lang/System", "currentTimeMillis", "()J");//load to operand stack
        methodVisitor.visitVarInsn(LSTORE, 2);
        methodVisitor.visitVarInsn(ALOAD, 0);
        methodVisitor.visitVarInsn(ALOAD, 1);
        methodVisitor.visitMethodInsn(INVOKESPECIAL, "org/example/Example", "run", "(Ljava/lang/String;)V");
        methodVisitor.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;"); //put System.out to operand stack
        methodVisitor.visitMethodInsn(INVOKESTATIC, "java/lang/System", "currentTimeMillis", "()J");
        methodVisitor.visitVarInsn(LLOAD, 2);
        methodVisitor.visitInsn(LSUB);
        methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(J)V");
        methodVisitor.visitInsn(RETURN);
        methodVisitor.visitMaxs(5, 4);
        methodVisitor.visitEnd();

        cw.visitEnd();
        //save bytecode into disk
        FileOutputStream out = new FileOutputStream("/Users/ekaterina/IdeaProjects/ASMGradleTest/src/main/java/gen/HelloGen.class");
        out.write(cw.toByteArray());
        out.close();
    }
}
