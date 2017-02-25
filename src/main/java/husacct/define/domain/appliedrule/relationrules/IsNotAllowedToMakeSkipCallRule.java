package husacct.define.domain.appliedrule.relationrules;

import husacct.define.domain.appliedrule.AppliedRuleStrategy;
import husacct.define.domain.conventions_checker.LayerCheckerHelper;
import husacct.define.domain.conventions_checker.ModuleCheckerHelper;
import husacct.define.domain.module.ModuleStrategy;
import husacct.define.domain.module.modules.Layer;

import java.util.ArrayList;

public class IsNotAllowedToMakeSkipCallRule extends AppliedRuleStrategy{
	private ModuleCheckerHelper moduleCheckerHelper;
	private LayerCheckerHelper layerCheckerHelper;

	public boolean checkConvention() {
		moduleCheckerHelper = new ModuleCheckerHelper();
		layerCheckerHelper = new LayerCheckerHelper(this.getModuleTo());

		if (!moduleCheckerHelper.rootIsNotIncludedInRule(getModuleFrom(), getModuleTo())){
			return false;
		}
		boolean conventionSuccess = moduleCheckerHelper.checkRuleTypeAlreadySet(
				this.getRuleTypeKey(), this.getModuleFrom());
		if (conventionSuccess) {
			conventionSuccess = layerCheckerHelper.checkTypeIsLayer(this.getModuleFrom());
		}
/*		if (conventionSuccess) {
			ArrayList<ModuleStrategy> skipCallLayers = layerCheckerHelper
					.getSkipCallLayers(this.getModuleFrom().getId());
			for (ModuleStrategy skipCallLayer : skipCallLayers) {
				this.setModuleTo(skipCallLayer);
				if (!checkIsNotAllowedToUse()) {
					conventionSuccess = false;
					break;
				}
			}
		} */
		return conventionSuccess;
	}

	private boolean checkIsNotAllowedToUse() {
		boolean isNotAllowedToUseSucces = moduleCheckerHelper
				.checkRuleTypeAlreadyFromThisToSelected("IsOnlyAllowedToUse",
						this.getModuleFrom(), this.getModuleTo());
		if (isNotAllowedToUseSucces) {
			isNotAllowedToUseSucces = moduleCheckerHelper
					.checkRuleTypeAlreadyFromThisToSelected(
							"IsTheOnlyModuleAllowedToUse", this.getModuleFrom(), this.getModuleTo());
		}
		if (isNotAllowedToUseSucces) {
			isNotAllowedToUseSucces = moduleCheckerHelper
					.checkRuleTypeAlreadyFromThisToSelected("IsAllowedToUse",
							this.getModuleFrom(), this.getModuleTo());
		}
		if (isNotAllowedToUseSucces) {
			isNotAllowedToUseSucces = moduleCheckerHelper
					.checkRuleTypeAlreadyFromThisToSelected("MustUse",
							this.getModuleFrom(), this.getModuleTo());
		}
		if (isNotAllowedToUseSucces
				&& layerCheckerHelper.checkTypeIsLayer(this.getModuleFrom())
				&& layerCheckerHelper.checkTypeIsLayer(this.getModuleTo())) {
			ArrayList<ModuleStrategy> skipCallLayers = layerCheckerHelper
					.getSkipCallLayers(this.getModuleFrom().getId());
			for (ModuleStrategy skipCallLayer : skipCallLayers) {
				if (skipCallLayer.equals(this.getModuleTo())) {
					isNotAllowedToUseSucces = moduleCheckerHelper
							.checkRuleTypeAlreadySet(
									"IsNotAllowedToMakeSkipCall", this.getModuleFrom());
				}
			}
		}
		return isNotAllowedToUseSucces;
	}

}