import java.util.List;
import java.util.ArrayList;

import java.lang.reflect.*;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public class Main {

    public static void main(String... args) {
        Course c1 = new Course("Databases", "CS101");
        Course c2 = new Course("Introduction to programming", "CS102");

        Student student = new Student("John", "Smith", 3.8f);
        student.enroll(c1);
        student.enroll(c2);

        Validator validator = new Validator();
        if(!validator.isValid(student)) {
            throw new IllegalArgumentException();
        }
    }

}

class Student {

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    @Valid(minValue = 0.0f, maxValue = 4.0f)
    private float gpa;

    @NotEmpty
    private List<Course> courses;


    Student(String firstName, String lastName, float gpa){
    	this.firstName = firstName;
    	this.lastName = lastName;
    	this.gpa = gpa;
    	courses = new ArrayList<Course>();
    }

    String getFirstName(){
    	return this.firstName;
    }

    String lastName(){
    	return this.lastName;
    }

    float getGPA(){
        return this.gpa;
    }

    List<Course> getCourses(){
        return new ArrayList<Course>(courses);
    }

    void enroll(Course course) {
        courses.add(course);
    }

}

class Course {

    private String name;

    private String code;

    Course(String name, String code){
    	this.name = name;
    	this.code = code;
    }

    String getName(){
    	return this.name;
    }
    String getCode(){
    	return this.code;
    }
}

class Validator {
    boolean isValid(Object object) {
        Class clazz = object.getClass();
         try {
			Field[] fields = clazz.getDeclaredFields(); 

			 for (Field field: fields) {
	            field.setAccessible(true);

	        	if(field.isAnnotationPresent(NotNull.class)){
	        		String value = (String) field.get(object);
	        		if(value.equals(null)){
	        			return false;
	        		}
	       		}
	       		else if(field.isAnnotationPresent(NotEmpty.class)){
	       			if(((List)field.get(object)).size() == 0){
                 		  return false;
 					 }
	       		}
	       		else if(field.isAnnotationPresent(Valid.class)){
	       			 Valid anno = field.getAnnotation(Valid.class);
	       			 
	       			 float min = anno.minValue();
					 float max = anno.maxValue();
					 Float value = (Float)field.get(object);
					 if(value<min && value>max)
					 	return false;
	       		}
	       	}
        }catch(IllegalAccessException e){

        }
        return true;           
	}
}


@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@interface NotNull {
}


@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@interface Valid {
	 public float minValue(); 
     public float maxValue(); 
}

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@interface NotEmpty {
}