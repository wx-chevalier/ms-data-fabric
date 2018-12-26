{{/* vim: set filetype=mustache: */}}
{{/* Expand the name of the chart. */}}
{{- define "dmc-proxy.name" -}}
{{- default .Chart.Name .Values.nameOverride | trunc 63 | trimSuffix "-" -}}
{{- end -}}

{{/*
 Create a default fully qualified app name.
 We truncate at 63 chars because some Kubernetes name fields are limited to this (by the DNS naming spec).
 If release name contains chart name it will be used as a full name.
 */}}
{{- define "dmc-proxy.fullname" -}}
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
{{- define "dmc-proxy.chart" -}}
{{- printf "%s-%s" .Chart.Name .Chart.Version | replace "+" "_" | trunc 63 | trimSuffix "-" -}}
{{- end -}}

{{/* 应用端口 */}}
{{- define "dmc-proxy.port" -}}
{{- default "80" .Values.config.server.port -}}
{{- end -}}

{{/* 存活性判断地址 */}}
{{- define "dmc-proxy.probePath" -}}
{{- printf "%s%s" .Values.config.server.servlet.path .Values.container.probePath -}}
{{- end -}}

{{/* Spring 配置路径 */}}
{{- define "dmc-proxy.config-path" -}}
{{- printf "%s" .Values.container.config.path -}}
{{- end -}}
