/*
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *
 */
define(["dojo/dom",
        "dojo/query",
        "dojo/_base/array",
        "dijit/registry",
        "qpid/common/util",
        "dojo/parser",
        "dojo/text!store/sitespecifictruststore/add.html",
        "dojo/domReady!"], function (dom, query, array, registry, util, parser, template)
       {
           var addKeyStore = {
               show: function (data)
               {
                   var that = this;
                   this.metadata = data.metadata
                   this.containerNode = data.containerNode;
                   data.containerNode.innerHTML = template;
                   parser.parse(this.containerNode).then(function (instances)
                                                         {

                                                             if (data.effectiveData)
                                                             {
                                                                 that.update(data.effectiveData);
                                                             }

                                                             util.applyMetadataToWidgets(data.containerNode,
                                                                                         "TrustStore",
                                                                                         "SiteSpecificTrustStore",
                                                                                         data.metadata);
                                                             if (data.effectiveData)
                                                             {
                                                                 util.disableWidgetsForImmutableFields(data.containerNode,
                                                                                                       "TrustStore",
                                                                                                       "SiteSpecificTrustStore",
                                                                                                       data.metadata);
                                                             }
                                                         });
               },
               update: function (effectiveData)
               {
                   var attributes = this.metadata.getMetaData("TrustStore", "SiteSpecificTrustStore").attributes;
                   var widgets = registry.findWidgets(this.containerNode);
                   array.forEach(widgets, function (item)
                   {
                       var name = item.id.replace("addStore.", "");
                       if (name in attributes)
                       {
                           var attribute = attributes[name];
                           var value = effectiveData[name];
                           if (value)
                           {
                               if (attribute.secure)
                               {
                                   if (!/^\*+/.test(value))
                                   {
                                       item.set("value", value);
                                   }
                                   else
                                   {
                                       item.set("placeHolder", value);
                                       item.set("required", false);
                                   }
                               }
                               else
                               {
                                   item.set("value", value);
                               }
                           }
                       }
                   });
               }
           };

           return addKeyStore;
       });
