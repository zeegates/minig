<table class="panelActions" cellspacing="0" cellpadding="0">
    <tbody>
    <tr>
        <td align="left" style="vertical-align: top;" ng-show="showUploadButton">
            <form enctype="multipart/form-data" method="post">
                <table cellspacing="1" cellpadding="0">
                    <tbody>
                    <tr>
                        <td width="" height="" align="left" style="vertical-align: middle;" rowspan="1">
                            <div class="gwt-Label"></div>
                        </td>
                        <td width="" height="" align="left" style="vertical-align: top;">
                            <input type="file" class="gwt-FileUpload" ng-model="file" ng-change="blur()" onchange="angular.element(this).scope().blur(this)">
                        </td>
                        <td width="" height="" align="left" style="vertical-align: top;" rowspan="1">
                            <table cellspacing="0" cellpadding="0">
                                <tbody>
                                <tr>
                                    <td align="left" style="vertical-align: top;">
                                        <img src="resources/images/spinner_moz.gif" class="gwt-Image" style="display: none;"
                                            aria-hidden="true">
                                    </td>
                                    <td align="left" style="vertical-align: top;"><a class="gwt-Anchor"
                                                                                     href="javascript:;">Delete</a></td>
                                </tr>
                                </tbody>
                            </table>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </form>
        </td>
    </tr>
    <tr>
        <td align="left" style="vertical-align: top;">
            <table cellspacing="0" cellpadding="0" class="panelActions">
                <tbody>
                <tr>
                    <td align="left" style="vertical-align: top;">
                        <div class="gwt-Label"></div>
                    </td>
                    <td align="left" style="vertical-align: top;">
                        <a class="gwt-Anchor" ng-click="showUploadButton = true" ng-hide="showUploadButton">Attach a file</a>
                        <a class="gwt-Anchor" ng-show="showUploadButton">Attach another file</a>
                    </td>
                </tr>
                </tbody>
            </table>
        </td>
    </tr>
    </tbody>
</table>


