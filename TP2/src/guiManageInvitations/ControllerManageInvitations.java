package guiManageInvitations;

import database.Database;

/*******
 * <p> Title: ControllerManageInvitations Class. </p>
 * 
 * <p> Description: Controller for the Manage Invitations page.</p>
 *
 */

public class ControllerManageInvitations {
	
	private static Database theDatabase = applicationMain.FoundationsMain.database;
	
	/**
	 * Instantiates a new controller manage invitations.
	 */
	public ControllerManageInvitations() {
	}
	
	/**
	 * Perform delete invitation.
	 */
	protected static void performDeleteInvitation() {
		ViewManageInvitations.InvitationDisplay selected = 
			ViewManageInvitations.table_Invitations.getSelectionModel().getSelectedItem();
		
		if (selected == null) {
			ViewManageInvitations.showAlert("No Selection", 
				"Please select an invitation to delete.");
			return;
		}
		
		boolean confirm = ViewManageInvitations.showConfirmation(
			"Confirm Delete", 
			"Are you sure you want to delete the invitation for " + selected.getEmail() + "?");
		
		if (confirm) {
			boolean success = theDatabase.deleteInvitationCode(selected.getCode());
			
			if (success) {
				ViewManageInvitations.showAlert("Success", "Invitation deleted successfully.");
				ViewManageInvitations.invitationData.remove(selected);
			} else {
				ViewManageInvitations.showAlert("Error", "Failed to delete invitation.");
			}
		}
	}
	
	/**
	 * Perform return.
	 */
	protected static void performReturn() {
		guiAdminHome.ViewAdminHome.displayAdminHome(ViewManageInvitations.theStage, ViewManageInvitations.theUser);
	}
	
	/**
	 * Perform logout.
	 */
	protected static void performLogout() {
		guiUserLogin.ViewUserLogin.displayUserLogin(ViewManageInvitations.theStage);
	}
	
	/**
	 * Perform quit.
	 */
	protected static void performQuit() {
		System.exit(0);
	}
}