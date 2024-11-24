// /**
//  * NOTE: This class is auto generated by the swagger code generator program (3.0.62).
//  * https://github.com/swagger-api/swagger-codegen
//  * Do not edit the class manually.
//  */
// package es.unex.swagger.api;


// import es.unex.swagger.model.Favorites;
// import es.unex.swagger.model.User;
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
// public interface FavoritesApi {

//     @Operation(summary = "Elimina un content de la lista de favorites del user, obteniendolo mediante la cookie de la sesión.", description = "Elimina un content de la lista de favorites del user", security = {
//         @SecurityRequirement(name = "cookieAuth"),
// @SecurityRequirement(name = "medifli_auth", scopes = {
//             "write:users",
// "read:users"        })    }, tags={ "favorites" })
//     @ApiResponses(value = { 
//         @ApiResponse(responseCode = "204", description = "Operación exitosa", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Favorites.class))),
        
//         @ApiResponse(responseCode = "400", description = "User no encontrado"),
        
//         @ApiResponse(responseCode = "404", description = "Valor no soportado") })
//     @RequestMapping(value = "/favorites/{idContent}",
//         produces = { "application/json", "application/xml", "application/x-www-form-urlencoded" }, 
//         method = RequestMethod.DELETE)
//     ResponseEntity<Favorites> deleteFavoritesByidContent(@Parameter(in = ParameterIn.PATH, description = "", required=true, schema=@Schema()) @PathVariable("idContent") Integer idContent
// , @Parameter(in = ParameterIn.COOKIE, description = "" ,required=true,schema=@Schema()) @CookieValue(value="SessionUserCookie", required=true) User sessionUserCookie
// );


//     @Operation(summary = "Devuelve la información de un content de la lista de favorites del user, obteniendolo mediante la cookie de la sesión, y usando el id establecido en el path del content a buscar en la lista.", description = "Devuelve la información de un content de la lista de favorites del user, dado el id del content en el path", security = {
//         @SecurityRequirement(name = "cookieAuth"),
// @SecurityRequirement(name = "medifli_auth", scopes = {
//             "write:users",
// "read:users"        })    }, tags={ "favorites" })
//     @ApiResponses(value = { 
//         @ApiResponse(responseCode = "200", description = "Operación exitosa", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Favorites.class)))),
        
//         @ApiResponse(responseCode = "400", description = "User no encontrado"),
        
//         @ApiResponse(responseCode = "404", description = "Valor no soportado") })
//     @RequestMapping(value = "/favorites/{idContent}",
//         produces = { "application/json", "application/xml" }, 
//         method = RequestMethod.GET)
//     ResponseEntity<List<Favorites>> getFavoritesByidContent(@Parameter(in = ParameterIn.PATH, description = "", required=true, schema=@Schema()) @PathVariable("idContent") Integer idContent
// , @Parameter(in = ParameterIn.COOKIE, description = "" ,required=true,schema=@Schema()) @CookieValue(value="SessionUserCookie", required=true) User sessionUserCookie
// );


//     @Operation(summary = "Devuelve la lista de favorites del user, obteniendolo mediante la cookie de la sesión.", description = "Devuelve la lista de favorites del user", security = {
//         @SecurityRequirement(name = "cookieAuth"),
// @SecurityRequirement(name = "medifli_auth", scopes = {
//             "write:users",
// "read:users"        })    }, tags={ "favorites" })
//     @ApiResponses(value = { 
//         @ApiResponse(responseCode = "200", description = "Operación exitosa", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Favorites.class)))),
        
//         @ApiResponse(responseCode = "400", description = "User no encontrado"),
        
//         @ApiResponse(responseCode = "404", description = "Valor no soportado") })
//     @RequestMapping(value = "/favorites",
//         produces = { "application/json", "application/xml" }, 
//         method = RequestMethod.GET)
//     ResponseEntity<List<Favorites>> getFavoritesByUserCookie(@Parameter(in = ParameterIn.COOKIE, description = "" ,required=true,schema=@Schema()) @CookieValue(value="SessionUserCookie", required=true) User sessionUserCookie
// );


//     @Operation(summary = "Añade un nuevo content a la lista de favorites del user, obteniendolo mediante la cookie de la sesión.", description = "Añade un nuevo content a la lista de favorites del user, dado el id del content en el path", security = {
//         @SecurityRequirement(name = "cookieAuth"),
// @SecurityRequirement(name = "medifli_auth", scopes = {
//             "write:users",
// "read:users"        })    }, tags={ "favorites" })
//     @ApiResponses(value = { 
//         @ApiResponse(responseCode = "201", description = "Operación exitosa", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Favorites.class))),
        
//         @ApiResponse(responseCode = "400", description = "Valor no soportado"),
        
//         @ApiResponse(responseCode = "404", description = "Favorito no encontrado.") })
//     @RequestMapping(value = "/favorites/{idContent}",
//         produces = { "application/json", "application/xml" }, 
//         method = RequestMethod.POST)
//     ResponseEntity<Favorites> postFavoritesByidContent(@Parameter(in = ParameterIn.PATH, description = "", required=true, schema=@Schema()) @PathVariable("idContent") Integer idContent
// , @Parameter(in = ParameterIn.COOKIE, description = "" ,required=true,schema=@Schema()) @CookieValue(value="SessionUserCookie", required=true) User sessionUserCookie
// );


//     @Operation(summary = "Añade un nuevo content a la lista de favorites del user, obteniendolo mediante la cookie de la sesión.", description = "Añade un nuevo content a la lista de favorites del user", security = {
//         @SecurityRequirement(name = "cookieAuth"),
// @SecurityRequirement(name = "medifli_auth", scopes = {
//             "write:users",
// "read:users"        })    }, tags={ "favorites" })
//     @ApiResponses(value = { 
//         @ApiResponse(responseCode = "201", description = "Operación exitosa", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Favorites.class))),
        
//         @ApiResponse(responseCode = "400", description = "Valor no soportado"),
        
//         @ApiResponse(responseCode = "404", description = "Favorito no encontrado.") })
//     @RequestMapping(value = "/favorites",
//         produces = { "application/json", "application/xml" }, 
//         consumes = { "application/json", "application/xml", "application/x-www-form-urlencoded" }, 
//         method = RequestMethod.POST)
//     ResponseEntity<Favorites> postFavoritesByUserCookie(@Parameter(in = ParameterIn.COOKIE, description = "" ,required=true,schema=@Schema()) @CookieValue(value="SessionUserCookie", required=true) User sessionUserCookie
// , @Parameter(in = ParameterIn.DEFAULT, description = "", schema=@Schema()) @Valid @RequestBody Favorites body
// );

// }

