package com.pintabar.webservices.apis;

import com.pintabar.persistence.dto.PurchasePurchaseOrderDTO;
import com.pintabar.persistence.dto.TableUnitDTO;
import com.pintabar.persistence.dto.UserDTO;
import com.pintabar.services.BusinessService;
import com.pintabar.services.TableUnitService;
import com.pintabar.services.UserService;
import com.pintabar.webservices.response.errors.ErrorCode;
import com.pintabar.webservices.response.errors.ResponseErrorHandler;
import org.springframework.stereotype.Component;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import java.util.Optional;

/**
 * Created by lucasgodoy on 13/06/17.
 */
@Component
@Path("/business")
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class BusinessAPI {

	private final UserService userService;
	private final TableUnitService tableUnitService;
	private final BusinessService businessService;
	private final ResponseErrorHandler responseErrorHandler;

	public BusinessAPI(UserService userService, TableUnitService tableUnitService,
	                   BusinessService businessService, ResponseErrorHandler responseErrorHandler) {
		this.userService = userService;
		this.tableUnitService = tableUnitService;
		this.businessService = businessService;
		this.responseErrorHandler = responseErrorHandler;
	}

	@POST
	@Path("/checkin/user/{userUuid}/tableUnit/{tableUnitUuid}/")
	public Response userCheckin(
			@PathParam("userUuid") String userUuid,
			@PathParam("tableUnitUuid") String tableUnitUuid,
			@Context UriInfo uriInfo) {
		Optional<UserDTO> userDTO = userService.getUser(userUuid);
		Optional<TableUnitDTO> tableUnitDTO = tableUnitService.getTableUnit(tableUnitUuid);
		if (!userDTO.isPresent()) {
			return responseErrorHandler.getResponse(Response.Status.NOT_FOUND, ErrorCode.USER_NOT_FOUND);
		} else if (!tableUnitDTO.isPresent()) {
			return responseErrorHandler.getResponse(Response.Status.NOT_FOUND, ErrorCode.TABLE_UNIT_NOT_FOUND);
		}
		Optional<PurchasePurchaseOrderDTO> purchaseOrder = businessService.checkInUserToTable(userDTO.get(), tableUnitDTO.get());
		if (purchaseOrder.isPresent()) {
			UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder();
			uriBuilder.path(purchaseOrder.get().getUuid());
			return Response.status(Response.Status.CREATED)
					.entity(purchaseOrder.get())
					.build();
		}
		return responseErrorHandler.getResponse(Response.Status.INTERNAL_SERVER_ERROR, ErrorCode.PURCHASE_ORDER_NOT_CREATED);
	}
}
