'use strict';

$(document).ready(function(){
	$(".toast").toast({delay: 2000});	// initialize "Changes Saved" toast message with 2 second time on screen when called
});


// listen for form input selections by clicking
$(".resumeFormField").on("click", function(){			
	showFormSaveButton($(this));
});

// listen for form input selections by means other than clicking (eg. tab key)
$(".resumeFormField").select(function(){				
	showFormSaveButton($(this));
});

// when a form input has been selected, show save button for that input
function showFormSaveButton(selectedFormField){
	$(".saveResumeChangesButton").hide()			// hide save buttons for all fields
	selectedFormField.parent().find(".saveResumeChangesButton").show();		 // only show save button for this specific field
}

// listen for changes in form input fields 
$(".resumeFormField").change(function() {
	saveChanges($(this));
});

// listen for manual clicks on save changes buttons
$(".saveResumeChangesButton").on("click", function(){
	let formElement = $(this).parent().find(".resumeFormField");
	saveChanges(formElement);
})


// create AJAX PUT call for saving changes to database
function saveChanges(editedFormElement){
	editedFormElement.parent().find(".saveResumeChangesButton").hide();		// hide save button for this field (indicating to user that the changes have been saved anyway)
	let target = $("label[for='" + editedFormElement.attr("id") + "']");
	let labelLength = target.html().length;
	let changeTarget = target.html().substring(0, (labelLength));	// changeTarget is a string value that is passed to the toast popup informing the user that changes have been saved.
	showToast(changeTarget);										// call toast popup
	
	console.log("Changes detected in " + changeTarget);
	console.log("New value: " + editedFormElement.val());
	
	let form = editedFormElement.closest("form");
	let url = "/resumes/" + form.find(".resumeId").val();
	
	$.ajax({
		type: "PUT",
		contentType: "application/json",
		data: formDataToJson(form),
		url: url,
		success: function(data, textStatus, xhr) {
			console.log("HTTP response: " + xhr.status);
		},
		error: function(data, textStatus, xhr) {
			console.log("HTTP response: " + xhr.status);
		}
	});
}


// show toast popup for saved changes
function showToast(changeTarget) {
	$(".changesSavedToastMessage").html("Changes to " + changeTarget + " saved!");
	$(".changesSavedToast").toast("show");
}


// convert Basics & Location form data into usable JSON for ajax calls
function formDataToJson(targetForm) {
	let array = targetForm.serializeArray();
	let jsonString = "{";
	for (let i = 0; i < array.length; i++) {
		jsonString += '"' + array[i].name + '":"' + array[i].value + '"';
		if (i < array.length-1) {		// add separating commas for all except last item in array
			jsonString += ",";
		}
	}
	jsonString += "}";
	return jsonString;
}



// listen for edit position button clicks
$(".positionEditButton").on("click", function(){
	let positionRow = $(this).closest("tr"); 
	positionRow.find(".positionData").hide();
	positionRow.find(".editButtonColumn").hide();
	positionRow.find(".editPositionForm").show();
	positionRow.find(".saveEditButtonColumn").show();
	
});


//listen for save edited position button clicks
$(".saveEditedPositionButton").on("click", function(){
	
	let positionRow = $(this).closest("tr");				// find which position is being edited
	
	let positionId = positionRow.find(".positionId").val();	// positionId for url
	let url = "/positions/" + positionId;
	
	$.ajax({
		type: "PUT",
		contentType: "application/json",
		data: positionFormDataToJson(positionRow),
		url: url,
		success: function(data, textStatus, xhr) {
			console.log("HTTP response: " + xhr.status);
		},
		error: function(data, textStatus, xhr) {
			console.log("HTTP response: " + xhr.status);
		}
	});
	
	positionRow.find(".editPositionForm").hide();
	positionRow.find(".saveEditButtonColumn").hide();
	positionRow.find(".positionData").show();
	positionRow.find(".editButtonColumn").show();
	
});


// listen for add position buttons
$(".addPositionButton").on("click", function(){
	console.log("Add position clicked!");
	let tableBody = $(this).closest("table").find("tbody");
	tableBody.find("tr").last().clone(true).appendTo(tableBody);	// clone last existing position row 
	
	let newRow = tableBody.find("tr").last();			// find new row
	newRow.find(".positionData").hide();				// hide position data
	newRow.find(".editButtonColumn").hide();			// hide edit button
	newRow.find(".editPositionForm").show();			// show edit position form
	let saveButton = newRow.find(".saveNewPositionButton");
	saveButton.show();												// show "save new position" button
	saveButton.parent().find(".saveEditedPositionButton").hide();	// hide "save edited position" button
	saveButton.parent().show();

	let cancelButton = newRow.find(".cancelNewPositionButton");
	cancelButton.show();											// show "cancel new position" button
	cancelButton.parent().find(".positionDeleteButton").hide();		// hide "delete existing position" button
});

$(".saveNewPositionButton").on("click", function() {
	console.log("create new position!");
	let positionRow = $(this).closest("tr");
	let url = "/positions";
	
	$.ajax({
		type: "POST",
		contentType: "application/json",
		data: positionFormDataToJson(positionRow),
		url: url,
		success: function(data, textStatus, xhr) {
			console.log("HTTP response: " + xhr.status);
		},
		error: function(data, textStatus, xhr) {
			console.log("HTTP response: " + xhr.status);
		}
	});
	
	// todo: after POST return list view to normal
	
});

$(".cancelNewPositionButton").on("click", function() {
	console.log("cancel!");
	// todo: remove new row, return list view to normal
});


// convert new/edit position form data into usable JSON for ajax calls
function positionFormDataToJson(positionRow) {
	
	let positionTypeId = positionRow.find(".positionType").val();					// find positionTypeId for json
	let jsonString = '{"positionType":{"positionTypeId":' + positionTypeId + '},';
	
	let resumeId = positionRow.find(".resumeId").val();								// find resumeId for json
	jsonString += '"resume":{"resumeId":' + resumeId + '},';
	
	jsonString += '"companyName":"' + positionRow.find(".editPositionCompanyName").val() + '",';
	jsonString += '"positionName":"' + positionRow.find(".editPositionName").val() + '",';
	jsonString += '"companyWebsite":"' + positionRow.find(".editPositionCompanyWebsite").val() + '",';
	jsonString += '"startDate":"' + positionRow.find(".editPositionStartDate").val() + '",';
	jsonString += '"endDate":"' + positionRow.find(".editPositionEndDate").val() + '",';
	jsonString += '"summary":"' + positionRow.find(".editPositionSummary").val() + '"}';
	
	return jsonString;
	
}

// listen for delete position button clicks
$(".positionDeleteButton").on("click", function(){
	
	let positionRow = $(this).closest("tr"); 				// find which position is being deleted
	let positionId = positionRow.find(".positionId").val();	// find positionId for url
	
	let url = "/positions/" + positionId;
	$.ajax({
		type: "DELETE",
		url: url,
		success: function(data, textStatus, xhr) {
			console.log("HTTP response: " + xhr.status);
		},
		error: function(data, textStatus, xhr) {
			console.log("HTTP response: " + xhr.status);
		}
	}).then(function(){
		positionRow.remove();
	});
});

