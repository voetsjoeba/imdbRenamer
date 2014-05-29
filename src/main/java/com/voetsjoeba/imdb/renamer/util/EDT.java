package com.voetsjoeba.imdb.renamer.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>Specifies a method's Swing EDT affinity. In other words, allows you to specify whether a method should or should not
 * be executed on the Swing event dispatching thread.</p>
 * 
 * <p>By default, this annotation specifies that a method <em>must</em> execute on the Swing EDT thread.</p>
 * 
 * @author Jeroen
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.SOURCE)
public @interface EDT {
	/**
	 * <p>One of
	 *   <ul>
	 *     <li><tt>always</tt>: Always execute this method on the Swing EDT.</li>
	 *     <li><tt>never</tt>: Never execute this method on the Swing EDT.</li>
	 *     <li><tt>allow</tt>: Allow this method to be executed on the Swing EDT, but don't require it to do so. This 
	 *         is equivalent to not specifying this annotation at all, but is included as an option nonetheless for 
	 *         the purpose of explicitly specifying this intended behaviour.</li>
	 *   </ul> 
	 * </p>
	 */
	String value() default "always";
}
