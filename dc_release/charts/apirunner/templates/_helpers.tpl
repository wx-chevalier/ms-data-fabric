{{/* vim: set filetype=mustache: */}}
{{/*
 Expand the name of the chart.
 */}}
{{- define "springapp.name" -}}
{{- default .Chart.Name .Values.nameOverride | trunc 63 | trimSuffix "-" -}}
{{- end -}}

{{/*
 Create a default fully qualified app name.
 We truncate at 63 chars because some Kubernetes name fields are limited to this (by the DNS naming spec).
 If release name contains chart name it will be used as a full name.
 */}}
{{- define "springapp.fullname" -}}
{{- if .Values.fullnameOverride -}}
{{- .Values.fullnameOverride | trunc 63 | trimSuffix "-" -}}
{{- else -}}
{{- $name := default .Chart.Name .Values.nameOverride -}}
{{- if contains $name .Release.Name -}}
{{- .Release.Name | trunc 63 | trimSuffix "-" -}}
{{- else -}}
{{- printf "%s-%s" .Release.Name $name | trunc 63 | trimSuffix "-" -}}
{{- end -}}
{{- end -}}
{{- end -}}

{{/*
 Create chart name and version as used by the chart label.
 */}}
{{- define "springapp.chart" -}}
{{- printf "%s-%s" .Chart.Name .Chart.Version | replace "+" "_" | trunc 63 | trimSuffix "-" -}}
{{- end -}}

{{- define "springapp.port" -}}
{{- default "80" .Values.application.server.port -}}
{{- end -}}

{{- define "springapp.probePath" -}}
{{- printf "%s%s" .Values.application.server.servlet.path .Values.container.probePath -}}
{{- end -}}

{{/* Regular Spring Configuration  */}}
{{- define "springapp.config.rgl" -}}
{{- printf "%s/rgl/" .Values.container.config.path -}}
{{- end -}}

{{/* Secret Spring Configuration */}}
{{- define "springapp.config.sec" -}}
{{- printf "%s/sec/" .Values.container.config.path -}}
{{- end -}}

{{/* Catalog directory */}}
{{- define "springapp.config.catalog" -}}
{{- .Values.application.jdbc.datasource.config.dir -}}
{{- end -}}

{{- define "grpc.port" -}}
{{- default "6565" .Values.application.grpc.port -}}
{{- end -}}