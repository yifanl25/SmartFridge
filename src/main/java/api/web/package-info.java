/**
 * Official HTTP entry layer for the Spring Boot backend.
 * <p>
 * Only this package owns Spring MVC annotations, routes, request validation, and request/response
 * translation. It delegates application behavior downward into {@code controller}, then
 * {@code service}, then {@code model}.
 */
package api.web;
