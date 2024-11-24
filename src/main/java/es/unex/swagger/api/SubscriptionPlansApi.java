// /**
//  * NOTE: This class is auto generated by the swagger code generator program (3.0.62).
//  * https://github.com/swagger-api/swagger-codegen
//  * Do not edit the class manually.
//  */
// package es.unex.swagger.api;


// import es.unex.swagger.model.SubscriptionType;
// import io.swagger.v3.oas.annotations.Operation;
// import io.swagger.v3.oas.annotations.Parameter;
// import io.swagger.v3.oas.annotations.enums.ParameterIn;
// import io.swagger.v3.oas.annotations.responses.ApiResponses;
// import io.swagger.v3.oas.annotations.responses.ApiResponse;
// import io.swagger.v3.oas.annotations.media.ArraySchema;
// import io.swagger.v3.oas.annotations.media.Content;
// import io.swagger.v3.oas.annotations.media.Schema;
// import io.swagger.v3.oas.annotations.security.SecurityRequirement;
// import org.springframework.http.ResponseEntity;
// import org.springframework.validation.annotation.Validated;
// import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.web.bind.annotation.RequestHeader;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RequestMethod;
// import org.springframework.web.bind.annotation.RequestParam;
// import org.springframework.web.bind.annotation.RequestPart;
// import org.springframework.web.multipart.MultipartFile;
// import org.springframework.web.bind.annotation.CookieValue;

// import javax.validation.Valid;
// import javax.validation.constraints.*;
// import java.util.List;
// import java.util.Map;

// @javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2024-10-18T10:29:32.211856553Z[GMT]")
// @Validated
// public interface SubscriptionPlansApi {

//     @Operation(summary = "Elimina un tipo de subscription, válida en la aplicación de Mediflí.", description = "Elimina un tipo de subscription, válida en la aplicación de Mediflí.", security = {
//         @SecurityRequirement(name = "cookieAuth"),
// @SecurityRequirement(name = "medifli_auth", scopes = {
//             "write:users",
// "read:users"        })    }, tags={ "subscriptionPlans" })
//     @ApiResponses(value = { 
//         @ApiResponse(responseCode = "204", description = "Operación exitosa", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SubscriptionType.class))),
        
//         @ApiResponse(responseCode = "400", description = "Valor no soportado"),
        
//         @ApiResponse(responseCode = "404", description = "Plan de subscription no encontrado") })
//     @RequestMapping(value = "/subscriptionPlans/{idSubscriptionPlan}",
//         produces = { "application/json", "application/xml", "application/x-www-form-urlencoded" }, 
//         method = RequestMethod.DELETE)
//     ResponseEntity<SubscriptionType> deleteSubscriptionPlansByIdSubscritionPlan(@Parameter(in = ParameterIn.PATH, description = "El id del tipo de suscripción que se desea buscar.", required=true, schema=@Schema()) @PathVariable("idSubscriptionPlan") Integer idSubscriptionPlan
// );


//     @Operation(summary = "Devuelve la lista de todos los tipos de subscriptiones, validas en la aplicación de Mediflí.", description = "Devuelve la lista de todos los tipos de subscriptiones, validas en la aplicación de Mediflí.", security = {
//         @SecurityRequirement(name = "cookieAuth"),
// @SecurityRequirement(name = "medifli_auth", scopes = {
//             "write:users",
// "read:users"        })    }, tags={ "subscriptionPlans" })
//     @ApiResponses(value = { 
//         @ApiResponse(responseCode = "200", description = "Operación exitosa", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = SubscriptionType.class)))),
        
//         @ApiResponse(responseCode = "400", description = "Valor no soportado"),
        
//         @ApiResponse(responseCode = "404", description = "Lista de subscriptiones no encontrado") })
//     @RequestMapping(value = "/subscriptionPlans",
//         produces = { "application/json", "application/xml" }, 
//         method = RequestMethod.GET)
//     ResponseEntity<List<SubscriptionType>> getAllSubscriptionPlans();


//     @Operation(summary = "Devuelve un tipo de subscription, valida en la aplicación de Mediflí.", description = "Devuelve un tipo de subscription, valida en la aplicación de Mediflí.", security = {
//         @SecurityRequirement(name = "cookieAuth"),
// @SecurityRequirement(name = "medifli_auth", scopes = {
//             "write:users",
// "read:users"        })    }, tags={ "subscriptionPlans" })
//     @ApiResponses(value = { 
//         @ApiResponse(responseCode = "200", description = "Operación exitosa", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SubscriptionType.class))),
        
//         @ApiResponse(responseCode = "400", description = "Valor no soportado"),
        
//         @ApiResponse(responseCode = "404", description = "Plan de subscription no encontrado") })
//     @RequestMapping(value = "/subscriptionPlans/{idSubscriptionPlan}",
//         produces = { "application/json", "application/xml", "application/x-www-form-urlencoded" }, 
//         method = RequestMethod.GET)
//     ResponseEntity<SubscriptionType> getSubscriptionPlansByIdSubscritionPlan(@Parameter(in = ParameterIn.PATH, description = "El id del tipo de suscripción que se desea buscar.", required=true, schema=@Schema()) @PathVariable("idSubscriptionPlan") Integer idSubscriptionPlan
// );


//     @Operation(summary = "Añade un nuevo tipo de subscription, validas en la aplicación de Mediflí.", description = "Añade un nuevo tipo de subscription, validas en la aplicación de Mediflí.", security = {
//         @SecurityRequirement(name = "cookieAuth"),
// @SecurityRequirement(name = "medifli_auth", scopes = {
//             "write:users",
// "read:users"        })    }, tags={ "subscriptionPlans" })
//     @ApiResponses(value = { 
//         @ApiResponse(responseCode = "201", description = "Operación exitosa", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = SubscriptionType.class)))),
        
//         @ApiResponse(responseCode = "400", description = "Valor no soportado"),
        
//         @ApiResponse(responseCode = "404", description = "Lista de subscriptiones no encontrado") })
//     @RequestMapping(value = "/subscriptionPlans",
//         produces = { "application/json", "application/xml" }, 
//         consumes = { "application/json", "application/xml" }, 
//         method = RequestMethod.POST)
//     ResponseEntity<List<SubscriptionType>> postListSubscriptionPlans(@Parameter(in = ParameterIn.DEFAULT, description = "", required=true, schema=@Schema()) @Valid @RequestBody SubscriptionType body
// );


//     @Operation(summary = "Actualiza o añade un tipo de subscription, valida en la aplicación de Mediflí.", description = "Actualiza o añade un tipo de subscription, valida en la aplicación de Mediflí.", security = {
//         @SecurityRequirement(name = "cookieAuth"),
// @SecurityRequirement(name = "medifli_auth", scopes = {
//             "write:users",
// "read:users"        })    }, tags={ "subscriptionPlans" })
//     @ApiResponses(value = { 
//         @ApiResponse(responseCode = "201", description = "Operación exitosa", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SubscriptionType.class))),
        
//         @ApiResponse(responseCode = "400", description = "Valor no soportado"),
        
//         @ApiResponse(responseCode = "404", description = "Plan de subscription no encontrado") })
//     @RequestMapping(value = "/subscriptionPlans/{idSubscriptionPlan}",
//         produces = { "application/json", "application/xml", "application/x-www-form-urlencoded" }, 
//         consumes = { "application/json", "application/xml", "application/x-www-form-urlencoded" }, 
//         method = RequestMethod.PUT)
//     ResponseEntity<SubscriptionType> updateSubscriptionPlansByIdSubscritionPlan(@Parameter(in = ParameterIn.PATH, description = "El id del tipo de suscripción que se desea acceder.", required=true, schema=@Schema()) @PathVariable("idSubscriptionPlan") Integer idSubscriptionPlan
// , @Parameter(in = ParameterIn.DEFAULT, description = "", required=true, schema=@Schema()) @Valid @RequestBody SubscriptionType body
// );

// }

