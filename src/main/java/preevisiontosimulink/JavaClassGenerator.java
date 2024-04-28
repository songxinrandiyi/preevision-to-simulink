package preevisiontosimulink;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import org.eclipse.jdt.core.dom.*;

public class JavaClassGenerator {
    public static void main(String[] args) {
        // Define the package name and directory where you want to save the generated class
        String packageName = "preevisiontosimulink.library";
        String packageDirectory = "src/main/java/" + packageName.replace('.', '/');

        // Create the AST for a Java file
        AST ast = AST.newAST(AST.JLS16);

        // Create a CompilationUnit
        CompilationUnit cu = ast.newCompilationUnit();

        // Create a package declaration
        PackageDeclaration packageDeclaration = ast.newPackageDeclaration();
        packageDeclaration.setName(ast.newName(packageName));
        cu.setPackage(packageDeclaration);

        // Create a class declaration
        TypeDeclaration classDeclaration = ast.newTypeDeclaration();
        classDeclaration.setName(ast.newSimpleName("GeneratedClass"));
        classDeclaration.modifiers().add(ast.newModifier(Modifier.ModifierKeyword.PUBLIC_KEYWORD));

        // Create a method declaration
        MethodDeclaration methodDeclaration = ast.newMethodDeclaration();
        methodDeclaration.setName(ast.newSimpleName("generate"));
        methodDeclaration.modifiers().add(ast.newModifier(Modifier.ModifierKeyword.PUBLIC_KEYWORD));
        methodDeclaration.setReturnType2(ast.newPrimitiveType(PrimitiveType.INT));
        Block methodBody = ast.newBlock();
        methodDeclaration.setBody(methodBody);

        // Create a return statement in the method
        ReturnStatement returnStatement = ast.newReturnStatement();
        returnStatement.setExpression(ast.newNumberLiteral("42"));
        methodBody.statements().add(returnStatement);

        // Add the method to the class declaration
        classDeclaration.bodyDeclarations().add(methodDeclaration);

        // Add the class declaration to the CompilationUnit
        cu.types().add(classDeclaration);

        // Write the generated Java code to a file in the package directory
        String filePath = packageDirectory + "/GeneratedClass.java";
        try (FileWriter writer = new FileWriter(new File(filePath))) {
            writer.write(cu.toString());
            System.out.println("Generated class saved to: " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
